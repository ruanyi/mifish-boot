package com.ruanyi.mifish.x8583.msg;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import com.ruanyi.mifish.common.utils.BytesUtil;
import com.ruanyi.mifish.x8583.ISOField;
import com.ruanyi.mifish.x8583.annotation.BitMapField;
import com.ruanyi.mifish.x8583.annotation.FixedLenField;
import com.ruanyi.mifish.x8583.annotation.UnsizedField;
import com.ruanyi.mifish.x8583.context.ModelAnnotations;
import com.ruanyi.mifish.x8583.context.X8583ModelContext;
import com.ruanyi.mifish.x8583.field.BytesISOField;
import com.ruanyi.mifish.x8583.field.ISOBitMap;
import com.ruanyi.mifish.x8583.model.ISOMsg;
import com.ruanyi.mifish.x8583.model.IntEncodeType;
import com.ruanyi.mifish.x8583.utils.BCDUtil;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-24 11:19
 */
public final class ISOUnPackager {

    /**
     * pack
     *
     * @param field
     * @param isoField
     * @param charset
     * @return
     * @throws IllegalAccessException
     */
    public static Object unpack(Field field, ISOField isoField, Charset charset) {
        if (isoField == null || isoField.getValues() == null) {
            return null;
        }
        return null;
    }

    /**
     * unpack
     *
     * @param datas
     * @param startIndex
     * @param fixedLenField
     * @return
     */
    public static BytesISOField unpack(byte[] datas, int startIndex, FixedLenField fixedLenField) {
        byte[] values = BytesUtil.read(datas, startIndex, fixedLenField.length());
        // PaddDirect paddDirect = fixedLenField.paddingWay();
        IntEncodeType intEncodeType = fixedLenField.intEncodeType();
        byte[] msg = null;
        if (intEncodeType == IntEncodeType.COMPRESS_BCD) {
            char[] chars = BCDUtil.unZipBcd(values);
            String str = new String(chars);
            msg = str.getBytes();
        } else if (intEncodeType == IntEncodeType.UNCOMPRESS_BCD) {
            char[] chars = BCDUtil.unBcd(values);
            String str = new String(chars);
            msg = str.getBytes();
        } else {
            msg = values;
        }
        return new BytesISOField(fixedLenField.order(), msg);
    }

    /**
     * unpack
     * 
     * @param datas
     * @param startIndex
     * @param bitMapField
     * @return
     */
    public static ISOBitMap unpack(byte[] datas, int startIndex, BitMapField bitMapField) {
        byte b = BytesUtil.read(datas, startIndex, 1)[0];
        int flag = b;
        if (flag < 0) {
            byte[] values = BytesUtil.read(datas, startIndex, 16);
            ISOBitMap isoBitMap = new ISOBitMap(bitMapField.order(), values);
            return isoBitMap;
        } else {
            byte[] values = BytesUtil.read(datas, startIndex, 8);
            ISOBitMap isoBitMap = new ISOBitMap(bitMapField.order(), values);
            return isoBitMap;
        }
    }

    /**
     * unpack
     *
     * @param datas
     * @param startIndex
     * @param unsizedField
     * @return
     */
    public static BytesISOField unpack(byte[] datas, int startIndex, UnsizedField unsizedField) {
        int lenlen = unsizedField.lenlen();
        byte[] lbytes = BytesUtil.read(datas, lenlen);
        IntEncodeType intEncodeType = unsizedField.intEncodeType();
        byte[] msg = null;
        if (intEncodeType == IntEncodeType.COMPRESS_BCD) {
            int msgLen = Integer.parseInt(new String(BCDUtil.unZipBcd(lbytes)));
            byte[] values = BytesUtil.read(datas, startIndex + lenlen, msgLen);
            char[] chars = BCDUtil.unZipBcd(values);
            String str = new String(chars);
            msg = str.getBytes();
        } else if (intEncodeType == IntEncodeType.UNCOMPRESS_BCD) {
            int msgLen = Integer.parseInt(new String(BCDUtil.unBcd(lbytes)));
            byte[] values = BytesUtil.read(datas, startIndex + lenlen, msgLen);
            char[] chars = BCDUtil.unBcd(values);
            String str = new String(chars);
            msg = str.getBytes();
        } else {
            byte[] values = BytesUtil.read(datas, startIndex + lenlen, BytesUtil.toInteger(lbytes));
            msg = values;
        }
        return new BytesISOField(unsizedField.order(), msg);
    }

    /**
     * pack
     *
     * @param isoMsg
     * @param clazz
     * @param charset
     * @return
     */
    public static <T> T unpack(ISOMsg isoMsg, Class<T> clazz, Charset charset) {
        try {
            X8583ModelContext x8583ModelContext = X8583ModelContext.getInstance();
            ModelAnnotations modelAnnotations = x8583ModelContext.getModelAnnotations(clazz);
            T result = clazz.newInstance();
            Iterator<Map.Entry<Integer, ISOField>> iterator = isoMsg.getISOFieldIterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, ISOField> entry = iterator.next();
                Integer index = entry.getKey();
                ISOField isoField = entry.getValue();
                Field field = modelAnnotations.getField(index);
                field.setAccessible(true);
                Object value = unpack(field, isoField, charset);
                field.set(result, value);
            }
            return result;
        } catch (Exception ex) {
            return null;
        }
    }

    /** ISOUnPackager */
    private ISOUnPackager() {}
}
