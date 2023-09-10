package com.ruanyi.mifish.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * BytesUtil
 * 
 * @Creator ruanyi
 * @Create-Date 2010-12-1
 */
public final class BytesUtil {

    public final static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E'};

    /**
     * toBinaryString
     * 
     * @param values
     * @return
     */
    public static String toBinaryString(byte[] values) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < values.length; i++) {
            if ((values[i] & 0x80) != 0) {
                buffer.append("1");
            } else {
                buffer.append("0");
            }

            if ((values[i] & 0x40) != 0) {
                buffer.append("1");
            } else {
                buffer.append("0");
            }

            if ((values[i] & 0x20) != 0) {
                buffer.append("1");
            } else {
                buffer.append("0");
            }

            if ((values[i] & 0x10) != 0) {
                buffer.append("1");
            } else {
                buffer.append("0");
            }

            if ((values[i] & 0x08) != 0) {
                buffer.append("1");
            } else {
                buffer.append("0");
            }

            if ((values[i] & 0x04) != 0) {
                buffer.append("1");
            } else {
                buffer.append("0");
            }

            if ((values[i] & 0x02) != 0) {
                buffer.append("1");
            } else {
                buffer.append("0");
            }

            if ((values[i] & 0x01) != 0) {
                buffer.append("1");
            } else {
                buffer.append("0");
            }
        }
        return buffer.toString();
    }

    /**
     * isEmpty
     * 
     * @param values
     * @return
     */
    public static boolean isEmpty(byte[] values) {
        if (values == null || values.length == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * toHexString
     *
     * @param values
     * @return
     */
    public static String toHexString(byte[] values) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < values.length; i++) {
            byte t = (byte)(values[i] & 0x0F);
            byte k = (byte)((values[i] >> 4) & 0x0F);
            buffer.append(Integer.toHexString(k));
            buffer.append(Integer.toHexString(t));
        }
        return buffer.toString().toUpperCase();
    }

    /**
     * xor
     * 
     * @param right
     * @param left
     * @return
     */
    public static byte[] xor(byte[] right, byte[] left) {
        if (right.length != left.length) {
            throw new RuntimeException("XOR need equal length byte array ");
        }
        byte[] value = new byte[right.length];
        for (int i = 0; i < right.length; i++) {
            value[i] = (byte)(right[i] ^ left[i]);
        }
        return value;
    }

    /**
     * split
     * 
     * @param bytes
     * @param length
     * @return
     */
    public static List<byte[]> split(byte[] bytes, int length) {
        List<byte[]> values = new ArrayList<byte[]>();

        while (bytes.length >= length) {
            values.add(read(bytes, length));
            bytes = remove(bytes, length);
        }
        if (bytes.length > 0) {
            values.add(bytes);
        }
        return values;
    }

    public static byte[] leftPadd(byte[] bytes, byte filler, int length) {
        byte[] temp = new byte[length - bytes.length];
        for (int i = 0; i < (length - bytes.length); i++) {
            temp[i] = filler;
        }
        return join(temp, bytes);
    }

    public static byte[] rightPadd(byte[] bytes, byte filler, int length) {
        byte[] temp = new byte[length - bytes.length];
        for (int i = 0; i < (length - bytes.length); i++) {
            temp[i] = filler;
        }
        return join(bytes, temp);
    }

    public static int toInteger(byte[] bytes) {
        return Integer.parseInt(new String(bytes));
    }

    public static byte[] join(byte[] left, byte[] right) {
        byte[] temp = new byte[left.length + right.length];
        System.arraycopy(left, 0, temp, 0, left.length);
        System.arraycopy(right, 0, temp, left.length, right.length);
        return temp;
    }

    public static byte[] remove(byte[] bytes, int length) {
        byte[] temp = new byte[bytes.length - length];
        System.arraycopy(bytes, length, temp, 0, bytes.length - length);
        return temp;
    }

    public static byte[] read(byte[] bytes, int length) {
        byte[] temp = new byte[length];
        System.arraycopy(bytes, 0, temp, 0, length);
        return temp;
    }

    public static byte[] read(byte[] bytes, int startIndex, int length) {
        byte[] temp = new byte[length];
        System.arraycopy(bytes, startIndex, temp, 0, length);
        return temp;
    }

    public static String printHex(byte[] buffer, int offset, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            byte b = buffer[i + offset];
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(digits[(b >> 4) & 0x0F]);
            sb.append(digits[b & 0x0F]);
        }
        return sb.toString();
    }

    public static boolean equals(byte[] a1, byte[] a2) {
        if (a1.length != a2.length) {
            return false;
        }
        return equals(a1, 0, a2, 0, a1.length);
    }

    public static boolean equals(byte[] a1, int a1Offset, byte[] a2, int a2Offset, int length) {
        if (a1.length < a1Offset + length || a2.length < a2Offset + length) {
            return false;
        }
        while (length-- > 0) {
            if (a1[a1Offset++] != a2[a2Offset++]) {
                return false;
            }
        }
        return true;
    }

    /**
     * find offset bytes from message, return the first index of offset
     * 
     * @param message
     * @param offset
     * @return
     */
    public static int index(byte[] message, byte[] offset) {
        return index(message, 0, offset);
    }

    /**
     * find offset bytes from message, return the first index of offset
     * 
     * @param msg
     * @param offset
     * @return
     */
    public static int index(byte[] msg, int start, byte[] offset) {
        int msgLen = msg.length;
        int offsetLen = offset.length;
        /** 假如起始位置+offset的长度都比message的长度还要长，则返回-1 */
        if ((start + offsetLen) > msgLen) {
            return -1;
        }
        /** 正式比较 */
        for (int i = start; i < msgLen; i++) {
            // find first match byte.
            if (msg[i] == offset[0]) {
                // loop for match reset bytes
                int j = 0;
                for (j = 1; j < offsetLen; j++) {
                    // if length overflow
                    if (msgLen <= i + j) {
                        return -1;
                    } else {
                        // the next j byte match, continue to match reset
                        if (offset[j] == msg[i + j]) {
                            continue;
                        } else {// not match
                            break;
                        }
                    }
                }
                // all of offset bytes match
                if (offsetLen == j) {
                    return i;
                } else {// part of offset match, continue loop
                    continue;
                }
            }
        }
        return -1;
    }

    public static String printHex(byte[] buffer) {
        return printHex(buffer, 0, buffer.length);
    }

    public static byte[] init(byte value, int length) {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = value;
        }
        return bytes;
    }

    /**
     * insert desc into src at insertIndex.
     * 
     * @param src
     * @param insertIndex
     * @param dest
     * @return
     */
    public static byte[] insert(byte[] src, int insertIndex, byte[] dest) {
        // split src
        byte[] left = read(src, insertIndex);
        byte[] right = read(src, insertIndex, src.length - insertIndex);
        // join dest after left
        left = join(left, dest);
        // join new left and right
        return join(left, right);
    }

    /**
     * insert dest into src, after assign string.
     * <p>
     * if not find match assign string, do nothing, return src
     * 
     * @param src
     * @param assign
     * @param dest
     * @return
     */
    public static byte[] insert(byte[] src, byte[] assign, byte[] dest) {
        // can not find the match assign, return src
        if (-1 == index(src, assign)) {
            return src;
        }
        // get index of insert position.
        int insertInd = index(src, assign) + assign.length;
        // split src
        byte[] left = read(src, insertInd);
        byte[] right = read(src, insertInd, src.length - insertInd);
        // join dest after left
        left = join(left, dest);
        // join new left and right
        return join(left, right);
    }

    public static byte[] fromHexString(String value) {
        if (value == null || value.length() == 0) {
            return new byte[] {};
        }

        value = value.toLowerCase();

        char[] cs = value.toCharArray();

        byte[] retVal = new byte[cs.length / 2];

        for (int i = 0; i < cs.length;) {
            byte h = (byte)(Integer.parseInt(new String(new char[] {cs[i++]}), 16) << 4);
            byte l = (byte)(Integer.parseInt(new String(new char[] {cs[i++]}), 16));
            retVal[i / 2 - 1] = (byte)(h | l);
        }
        return retVal;
    }

    public static byte[] MacConvert(byte[] source) {
        byte[] result = new byte[8];
        for (int i = 0; i < 8; i++) {
            int t = source[2 * i];
            int k = source[2 * i + 1];

            if (t > '9') {
                t = t - 'A' + 0xa;
            } else {
                t = t - '0';
            }
            if (k > '9') {
                k = k - 'A' + 0xa;
            } else {
                k = k - '0';
            }
            result[i] = (byte)(0x10 * (t) + k);
        }

        return result;
    }

    /**
     * trim
     *
     * @param bytes
     * @return
     */
    public static byte[] trim(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return new byte[0];
        }
        int startloc = 0, endloc = bytes.length - 1;
        boolean lflag = true, rflag = true;
        for (int i = 0, j = endloc; i <= j;) {
            if (lflag) {
                if (bytes[i] == 32 || bytes[i] == 0) {
                    lflag = true;
                } else {
                    lflag = false;
                    startloc = i;
                }
            }
            if (rflag) {
                if (bytes[j] == 32 || bytes[j] == 0) {
                    rflag = true;
                } else {
                    rflag = false;
                    endloc = j;
                }
            }
            if (i == j || ((lflag == false) && (rflag == false))) {
                break;
            }
            if (lflag) {
                i++;
            }
            if (rflag) {
                j--;
            }
        }
        if (lflag && rflag) {
            return new byte[0];
        }
        byte[] result = new byte[endloc - startloc + 1];
        System.arraycopy(bytes, startloc, result, 0, endloc - startloc + 1);
        return result;
    }

    /**
     * leftTrim
     * 
     * @param bytes
     * @return
     */
    public static byte[] leftTrim(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return new byte[0];
        }
        int startloc = 0, len = bytes.length;
        boolean lflag = true;
        for (int i = 0; i < len; i++) {
            if (bytes[i] == 32 || bytes[i] == 0) {
                lflag = true;
                continue;
            } else {
                lflag = false;
                startloc = i;
                break;
            }
        }
        if (lflag) {
            return new byte[0];
        }
        byte[] result = new byte[len - startloc];
        // endloc-startloc+1,,len-1-startloc+1,,endloc = len-1
        System.arraycopy(bytes, startloc, result, 0, len - startloc);
        return result;
    }

    /**
     * rightTrim
     *
     * @param bytes
     * @return
     */
    public static byte[] rightTrim(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return new byte[0];
        }
        int endloc = bytes.length - 1;
        boolean rflag = true;
        for (int j = endloc; j >= 0; j--) {
            if (bytes[j] == 32 || bytes[j] == 0) {
                rflag = true;
                continue;
            } else {
                rflag = false;
                endloc = j;
                break;
            }
        }
        if (rflag) {
            return new byte[0];
        }
        byte[] result = new byte[endloc + 1];
        // enloc-startloc+1,startloc==0
        System.arraycopy(bytes, 0, result, 0, endloc + 1);
        return result;
    }

    private BytesUtil() {

    }
}
