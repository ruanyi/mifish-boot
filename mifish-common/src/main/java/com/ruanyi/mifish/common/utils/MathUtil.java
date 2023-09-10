package com.ruanyi.mifish.common.utils;

/**
 * MathUtil
 * 
 * @Creator ruanyi
 *
 * @CreateTime 2008-11-6
 *
 *  $Author$ ${date} $Revision$ $Date$
 */
public final class MathUtil {

    private MathUtil() {}

    /**
     * @param b
     * @return
     */
    public static int parseByteUnsigned(byte b) {
        if ((b & 0x80) != 0) {
            b = (byte)(b & ((byte)127));
            return 128 + b;
        } else {
            return b;
        }
    }

    public static int parseBytes(byte[] b) {
        int t = 0;
        int rst = 0;
        for (int i = 0; i < b.length; i++) {
            t = parseByteUnsigned(b[i]);
            if ((b.length - i - 1) > 0) {
                t = t << (8 * (b.length - i - 1));
            }
            rst += t;
        }
        return rst;
    }

    /**
     * 整形转成bytes 在java中int占了4个字节
     * 
     * @param number
     * @return
     */
    public static byte[] int2bytes(int number) {
        int temp1 = number >> (1 * 8);
        int temp2 = temp1 >> 8;
        int temp3 = temp2 >> 8;
        byte[] bytes = new byte[] {(byte)temp3, (byte)temp2, (byte)temp1, (byte)number};
        return bytes;
    }

    /**
     * 4位bytes转成整形 一定是4位的 在java中，整形占据4个字节
     */
    public static int bytes2Int(byte[] bytes) {
        if (bytes == null || bytes.length == 0 || bytes.length != 4) {
            return 0;
        }
        int number = (parseByteUnsigned(bytes[0])) << (3 * 8) | (parseByteUnsigned(bytes[1])) << (2 * 8)
            | (parseByteUnsigned(bytes[2])) << (8) | (parseByteUnsigned(bytes[3]));
        return number;
    }
}
