package com.ruanyi.mifish.x8583.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ruanyi.mifish.common.utils.BytesUtil;
import com.ruanyi.mifish.common.utils.MathUtil;

/**
 * 标准的银联55域处理工具
 * 
 * @author ruanyi
 * @time:2013-04-18
 */
public final class CupsF55Util {

    /** 不允许实例化 */
    private CupsF55Util() {}

    /** 解开55域 */
    public static Map<String, byte[]> unpack55(byte[] values) {
        Map<String, byte[]> retVal = new HashMap<>();
        byte[] tb = new byte[] {};
        byte[] lb = new byte[] {};
        byte[] vb = new byte[] {};
        int length = 0;
        for (int i = 0; i < values.length;) {
            // 判断TAG位数
            byte temp = values[i];
            if ((temp & 0x1F) != 0x1F) {
                tb = new byte[] {values[i]};
                i++;
            } else {
                tb = new byte[] {values[i], values[i + 1]};
                i++;
                i++;
            }
            // 获取长度
            temp = values[i];
            if ((temp & 0x80) == 0) {
                length = MathUtil.parseByteUnsigned(temp);
                i++;
            } else {
                int ll = MathUtil.parseByteUnsigned((byte)(temp & 0x7F));
                lb = BytesUtil.read(values, i + 1, ll);
                length = MathUtil.parseBytes(lb);
                i = i + ll + 1;
            }
            // 获取值
            vb = BytesUtil.read(values, i, length);
            retVal.put(BytesUtil.toHexString(tb), vb);
            i = i + length;
        }
        return retVal;
        /*
         * if (retVal.containsKey("9A")) { retVal.put("9A",
         * AntBCD(retVal.get("9A"))); } if (retVal.containsKey("9C")) {
         * retVal.put("9C", AntBCD(retVal.get("9C"))); } if
         * (retVal.containsKey("9F02")) { retVal.put("9F02",
         * AntBCD(retVal.get("9F02"))); } if (retVal.containsKey("5F2A")) {
         * retVal.put("5F2A", AntBCD(retVal.get("5F2A"))); } if
         * (retVal.containsKey("9F1A")) { retVal.put("9F1A",
         * AntBCD(retVal.get("9F1A"))); } if (retVal.containsKey("9F03")) {
         * retVal.put("9F03", AntBCD(retVal.get("9F03"))); } if
         * (retVal.containsKey("5F2A")) { retVal.put("9F35",
         * AntBCD(retVal.get("9F35"))); } if (retVal.containsKey("9F41")) {
         * retVal.put("9F41", AntBCD(retVal.get("9F41"))); }
         */
    }

    /** 55域打包 */
    public static byte[] pack55(Map<String, byte[]> values) throws IOException {
        /*
         * if (values.containsKey("9A")) { values.put("9A",
         * BCD(values.get("9A"))); } if (values.containsKey("9C")) {
         * values.put("9C", BCD(values.get("9C"))); } if
         * (values.containsKey("9F02")) { values.put("9F02",
         * BCD(values.get("9F02"))); } if (values.containsKey("5F2A")) {
         * values.put("5F2A", BCD(values.get("5F2A"))); } if
         * (values.containsKey("9F1A")) { values.put("9F1A",
         * BCD(values.get("9F1A"))); } if (values.containsKey("9F03")) {
         * values.put("9F03", BCD(values.get("9F03"))); } if
         * (values.containsKey("5F2A")) { values.put("9F35",
         * BCD(values.get("9F35"))); } if (values.containsKey("9F41")) {
         * values.put("9F41", BCD(values.get("9F41"))); }
         */
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        try {
            for (Map.Entry<String, byte[]> entry : values.entrySet()) {
                byte[] tb = BytesUtil.fromHexString(entry.getKey());
                byte[] vb = entry.getValue();
                if (vb == null || vb.length == 0) {
                    continue;
                }
                byte[] lb = new byte[] {};
                int length = vb.length;
                if (length > 127) {
                    lb = new byte[] {(byte)0x82, (byte)length};
                } else {
                    lb = new byte[] {(byte)length};
                }
                os.write(tb);
                os.write(lb);
                os.write(vb);
            }
            return os.toByteArray();
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }
}
