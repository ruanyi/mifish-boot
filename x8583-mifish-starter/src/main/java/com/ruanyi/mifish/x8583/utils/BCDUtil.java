package com.ruanyi.mifish.x8583.utils;

/**
 * only digital and charactor support bcd c-antc operation 仅仅只支持数字
 *
 * BCD压缩后，不能调用BytesUtil.trim()、BytesUtil.leftTrim()、BytesUtil.rightTrim();
 *
 * 否则，数据会丢失
 *
 * @Creator ruanyi
 * @CreateTime 2010-10-23 $Author$ ${date} $Revision$ $Date$
 */
public final class BCDUtil {

    private BCDUtil() {}

    /**
     * 默认左靠
     */
    public static byte[] pZipBcd(char[] array) {
        return pZipBcd(array, true);
    }

    /**
     * 打包压缩 BCD lflg：true：数据域左靠 右补0 lflg：false：数据域右靠，左补0
     */
    public static byte[] pZipBcd(char[] array, boolean lflg) {
        if (array == null || array.length == 0) {
            return new byte[0];
        }
        //
        int len = array.length;
        int bslen = len / 2 + len % 2;
        byte[] result = new byte[bslen];
        byte h, l;// h:高位、l：地位
        if (len % 2 == 0) {
            for (int i = 0; i < bslen; i++) {
                h = (byte)(nchar2byte(array[2 * i]) << 4);
                l = nchar2byte(array[2 * i + 1]);
                result[i] = (byte)(h | l);
            }
        } else {
            if (lflg) {
                for (int i = 0; i < bslen - 1; i++) {
                    h = (byte)(nchar2byte(array[2 * i]) << 4);
                    l = nchar2byte(array[2 * i + 1]);
                    result[i] = (byte)(h | l);
                }
                // 最后
                h = (byte)(nchar2byte(array[len - 1]) << 4);
                l = 0;
                result[bslen - 1] = (byte)(h | l);
            } else {
                // 最开始
                h = 0;
                l = nchar2byte(array[0]);
                result[0] = (byte)(h | l);
                for (int i = 1; i < bslen; i++) {
                    h = (byte)(nchar2byte(array[2 * i - 1]) << 4);
                    l = nchar2byte(array[2 * i]);
                    result[i] = (byte)(h | l);
                }
            }
        }
        /** 返回结果 */
        return result;
    }

    /**
     * 打包非压缩 型BCD
     */
    public static byte[] pBcd(char[] array) {
        if (array == null || array.length == 0) {
            return new byte[0];
        }
        //
        byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = nchar2byte(array[i]);

        }
        /** 返回结果 */
        return result;
    }

    /**
     * 将char转换成相应的二进制的byte 例如： char c = '0' 转换成二进制的byte：0000 0000 而不是 '0' 对应的十进制：48，二进制：0011 0000
     */
    public static byte nchar2byte(char c) {
        if (c <= '9' && c >= '0') {
            return (byte)(c - '0');
        } else if (c <= 'z' && c >= 'a') {
            return (byte)(c - 'a' + 10);
        } else {
            return (byte)(c - 'A' + 10);
        }
    }

    static final char[] HEX =
        new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 解开压缩型的BCD码
     */
    public static char[] unZipBcd(byte[] buffer) {
        if (buffer == null || buffer.length == 0) {
            return new char[0];
        }
        int holder = 0;
        char[] chars = new char[buffer.length * 2];
        for (int i = 0; i < buffer.length; i++) {
            holder = (buffer[i] & 0xf0) >> 4;
            chars[i * 2] = HEX[holder];
            holder = buffer[i] & 0x0f;
            chars[(i * 2) + 1] = HEX[holder];
        }
        return chars;
    }

    /**
     * 解开非压缩型的BCD码
     */
    public static char[] unBcd(byte[] buffer) {
        if (buffer == null || buffer.length == 0) {
            return new char[0];
        }
        int holder = 0;
        char[] chars = new char[buffer.length];
        for (int i = 0; i < buffer.length; i++) {
            holder = buffer[i];
            chars[i] = HEX[holder];
        }
        return chars;
    }
}
