package com.ruanyi.mifish.x8583.msg;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.common.utils.BytesUtil;
import com.ruanyi.mifish.x8583.ISOField;
import com.ruanyi.mifish.x8583.annotation.FixedLenField;
import com.ruanyi.mifish.x8583.annotation.UnsizedField;
import com.ruanyi.mifish.x8583.ex.ISOX8583Exception;
import com.ruanyi.mifish.x8583.field.ISOBitMap;
import com.ruanyi.mifish.x8583.model.ISOMsg;
import com.ruanyi.mifish.x8583.model.IntEncodeType;
import com.ruanyi.mifish.x8583.model.PaddDirect;
import com.ruanyi.mifish.x8583.utils.BCDUtil;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-24 11:18
 */
public final class ISOPackager {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /**
     * pack
     * 
     * @param field
     * @param object
     * @param charset
     * @return
     * @throws IllegalAccessException
     */
    public static byte[] pack(Field field, Object object, Charset charset) throws IllegalAccessException {
        if (object == null) {
            return null;
        }
        field.setAccessible(true);
        if (field.getType().isPrimitive()) {
            if (Integer.TYPE == field.getType()) {
                int v = field.getInt(object);
                byte[] intBytes = ByteBuffer.allocate(4).putInt(v).array();
                return intBytes;
            }
            if (Float.TYPE == field.getType()) {
                float v = field.getFloat(object);
                byte[] floatBytes = ByteBuffer.allocate(4).putFloat(v).array();
                return floatBytes;
            }
            if (Double.TYPE == field.getType()) {
                double v = field.getDouble(object);
                byte[] doubleBytes = ByteBuffer.allocate(8).putDouble(v).array();
                return doubleBytes;
            }
            if (Long.TYPE == field.getType()) {
                long v = field.getLong(object);
                byte[] longBytes = ByteBuffer.allocate(8).putLong(v).array();
                return longBytes;
            }
            if (Byte.TYPE == field.getType()) {
                byte v = field.getByte(object);
                return new byte[] {v};
            }
            if (Character.TYPE == field.getType()) {
                char c = field.getChar(object);
                byte[] charBytes = ByteBuffer.allocate(2).putChar(c).array();
                return charBytes;
            }
        }
        Object value = field.get(object);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            String str = (String)value;
            return str.getBytes(charset);
        } else {
            // 先转成string
            String str = value.toString();
            return str.getBytes(charset);
        }
    }

    /**
     * pack
     * 
     * @param datas
     * @param fixedLenField
     * @return
     */
    public static byte[] pack(byte[] datas, FixedLenField fixedLenField) {
        if (datas == null) {
            return null;
        }
        PaddDirect paddDirect = fixedLenField.paddingWay();
        IntEncodeType intEncodeType = fixedLenField.intEncodeType();
        if (intEncodeType == IntEncodeType.COMPRESS_BCD) {
            // bcd码，目前仅支持数字,因此，只会左补0 or 右补0
            if (paddDirect == PaddDirect.PADD_RIGHT) {
                return BCDUtil.pZipBcd(new String(datas).toCharArray(), false);
            } else {
                return BCDUtil.pZipBcd(new String(datas).toCharArray(), true);
            }
        } else if (intEncodeType == IntEncodeType.UNCOMPRESS_BCD) {
            // bcd码，目前仅支持数字
            if (paddDirect == PaddDirect.PADD_RIGHT) {
                byte[] newDatas = BytesUtil.rightPadd(datas, fixedLenField.paddingByte(), fixedLenField.length());
                return BCDUtil.pBcd(new String(newDatas).toCharArray());
            } else {
                byte[] newDatas = BytesUtil.leftPadd(datas, fixedLenField.paddingByte(), fixedLenField.length());
                return BCDUtil.pBcd(new String(newDatas).toCharArray());
            }
        } else {
            if (paddDirect == PaddDirect.PADD_RIGHT) {
                return BytesUtil.rightPadd(datas, fixedLenField.paddingByte(), fixedLenField.length());
            } else {
                return BytesUtil.leftPadd(datas, fixedLenField.paddingByte(), fixedLenField.length());
            }
        }
    }

    /**
     * pack
     * 
     * @param datas
     * @param unsizedField
     * @return
     */
    public static byte[] pack(byte[] datas, UnsizedField unsizedField) {
        if (datas == null) {
            return null;
        }
        int lenlen = unsizedField.lenlen();
        int datalen = datas.length;
        int realDatalenlen = (datalen + "").length();
        if (realDatalenlen > lenlen) {
            throw new ISOX8583Exception(
                "UnsizedField support max length is " + lenlen + ",current data len len is[" + realDatalenlen + "].");
        }
        byte[] left = BytesUtil.leftPadd((datalen + "").getBytes(), "0".getBytes()[0], lenlen);
        byte[] datasWithLen = BytesUtil.join(left, datas);
        IntEncodeType intEncodeType = unsizedField.intEncodeType();
        if (intEncodeType == IntEncodeType.COMPRESS_BCD) {
            return BCDUtil.pZipBcd(new String(datasWithLen).toCharArray(), true);
        } else if (intEncodeType == IntEncodeType.UNCOMPRESS_BCD) {
            return BCDUtil.pBcd(new String(datasWithLen).toCharArray());
        } else {
            return datasWithLen;
        }
    }

    /**
     * pack
     * 
     * @param isoMsg
     * @return
     */
    public static byte[] pack(ISOMsg isoMsg) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(2048);
        try {
            ISOField mti = isoMsg.getField(0);
            if (mti == null) {
                LOG.debug(Pair.of("clazz", "ISOPackager"), Pair.of("method", "pack"),
                    Pair.of("msg", "current 8583 bytes message,not exits MTI,ignore it"));
            } else {
                os.write(mti.getValues());
            }
            ISOBitMap bitmap = (ISOBitMap)isoMsg.getField(1);
            os.write(bitmap.getValues());
            Iterator<Map.Entry<Integer, ISOField>> iterator = isoMsg.getISOFieldIterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, ISOField> entry = iterator.next();
                Integer key = entry.getKey();
                if (key == 0 || key == 1) {
                    continue;
                }
                ISOField isoField = entry.getValue();
                os.write(isoField.getValues());
            }
            return os.toByteArray();
        } catch (Exception ex) {
            // ingore
            LOG.error(ex, Pair.of("clazz", "ISOPackager"), Pair.of("method", "pack"),
                Pair.of("msg", "pack iso msg occur unknow exception"));
            return null;
        }
    }

    /** ISOPackager */
    private ISOPackager() {}
}
