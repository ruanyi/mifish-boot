package com.ruanyi.mifish.x8583;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Map;

import com.ruanyi.mifish.x8583.annotation.BitMapField;
import com.ruanyi.mifish.x8583.annotation.FixedLenField;
import com.ruanyi.mifish.x8583.annotation.UnsizedField;
import com.ruanyi.mifish.x8583.context.ModelAnnotations;
import com.ruanyi.mifish.x8583.context.X8583ModelContext;
import com.ruanyi.mifish.x8583.field.BytesISOField;
import com.ruanyi.mifish.x8583.field.ISOBitMap;
import com.ruanyi.mifish.x8583.model.ISOMsg;
import com.ruanyi.mifish.x8583.model.X8583Model;
import com.ruanyi.mifish.x8583.msg.ISOPackager;
import com.ruanyi.mifish.x8583.msg.ISOUnPackager;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-10 15:28
 */
public class X8583Object {

    /**
     * toX8583Bytes
     * 
     * @param x8583Model
     * @param charset
     * @return
     */
    public static byte[] toX8583Bytes(X8583Model x8583Model, String charset) {
        return packX8583Bytes(x8583Model, charset);
    }

    /**
     * toX8583String
     *
     * @param x8583Model
     * @param charset
     * @return
     */
    public static String toX8583String(X8583Model x8583Model, String charset) {
        byte[] datas = toX8583Bytes(x8583Model, charset);
        try {
            return new String(datas, charset);
        } catch (UnsupportedEncodingException e) {
            return new String(datas);
        }
    }

    /**
     * packX8583Bytes
     * 
     * @param x8583Obj
     * @return
     */
    public static byte[] packX8583Bytes(Object x8583Obj, String charset) {
        try {
            Charset cs = Charset.forName(charset);
            X8583ModelContext x8583ModelContext = X8583ModelContext.getInstance();
            ModelAnnotations modelAnnotations = x8583ModelContext.getModelAnnotations(x8583Obj.getClass());
            // x8583Model -> ISOMsg
            ISOMsg isoMsg = new ISOMsg();
            BitSet bs = null;
            boolean flag = false;
            // 第一次遍历，确定BitMap
            Iterator<Map.Entry<Integer, Annotation>> itr1 = modelAnnotations.getAnnotationIterator();
            while (itr1.hasNext()) {
                Map.Entry<Integer, Annotation> entry = itr1.next();
                Integer index = entry.getKey();
                if (index > 64) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                bs = new BitSet(128);
                bs.set(1, true);
            } else {
                bs = new BitSet(64);
                bs.set(1, false);
            }
            // 第二次遍历，处理各个域
            Iterator<Map.Entry<Integer, Annotation>> iterator = modelAnnotations.getAnnotationIterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, Annotation> entry = iterator.next();
                Integer index = entry.getKey();
                Field field = modelAnnotations.getField(index);
                Annotation annotation = entry.getValue();
                if (annotation instanceof BitMapField) {
                    BitMapField bitMapField = (BitMapField)annotation;
                    isoMsg.setField(new ISOBitMap(bitMapField.order(), bs));
                    continue;
                }

                BytesISOField bytesISOField = null;
                if (annotation instanceof FixedLenField) {
                    FixedLenField fixedLenField = (FixedLenField)annotation;
                    byte[] datas = ISOPackager.pack(ISOPackager.pack(field, x8583Obj, cs), fixedLenField);
                    if (datas != null) {
                        bytesISOField = new BytesISOField(fixedLenField.order(), datas);
                    }
                } else if (annotation instanceof UnsizedField) {
                    UnsizedField unsizedField = (UnsizedField)annotation;
                    byte[] datas = ISOPackager.pack(ISOPackager.pack(field, x8583Obj, cs), unsizedField);
                    if (datas != null) {
                        bytesISOField = new BytesISOField(unsizedField.order(), datas);
                    }
                }
                if (bytesISOField != null) {
                    bs.set(index, true);
                    isoMsg.setField(bytesISOField);
                } else {
                    bs.set(index, false);
                }
            }
            return ISOPackager.pack(isoMsg);
        } catch (Exception ex) {
            // todo
            return null;
        }
    }

    /**
     * uppackX8583
     * 
     * @param x8583Bytes
     * @param <T>
     * @param charset
     * @return
     */
    public static <T> T uppackX8583(byte[] x8583Bytes, Class<T> clazz, String charset) {
        if (x8583Bytes == null) {
            return null;
        }
        X8583ModelContext x8583ModelContext = X8583ModelContext.getInstance();
        ModelAnnotations modelAnnotations = x8583ModelContext.getModelAnnotations(clazz);
        ISOMsg isoMsg = new ISOMsg();
        FixedLenField mtiField = modelAnnotations.getAnnotation(0, FixedLenField.class);
        int startIndex = 0;
        BytesISOField mtiISOField = ISOUnPackager.unpack(x8583Bytes, startIndex, mtiField);
        isoMsg.setField(mtiISOField);
        startIndex += mtiField.length();
        BitMapField bitMapField = modelAnnotations.getBitMapField();
        ISOBitMap isoBitMap = ISOUnPackager.unpack(x8583Bytes, startIndex, bitMapField);
        isoMsg.setField(isoBitMap);
        int valuesLen = isoBitMap.getValuesLength();
        startIndex += valuesLen;
        for (int i = 2, len = valuesLen * 8; i <= len; i++) {
            if (!isoBitMap.isExsits(i)) {
                continue;
            }
            Annotation annotation = modelAnnotations.getAnnotation(i);
            if (annotation instanceof FixedLenField) {
                FixedLenField fixedLenField = (FixedLenField)annotation;
                BytesISOField isoField = ISOUnPackager.unpack(x8583Bytes, startIndex, fixedLenField);
                startIndex += fixedLenField.length();
                isoMsg.setField(isoField);
            } else if (annotation instanceof UnsizedField) {
                UnsizedField unsizedField = (UnsizedField)annotation;
                BytesISOField isoField = ISOUnPackager.unpack(x8583Bytes, startIndex, unsizedField);
                startIndex = startIndex + unsizedField.lenlen() + isoField.getValues().length;
                isoMsg.setField(isoField);
            }
        }
        return ISOUnPackager.unpack(isoMsg, clazz, Charset.forName(charset));
    }
}
