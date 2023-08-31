package com.ruanyi.mifish.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2020-08-23 23:07
 */
public final class CharacterUtil {

    private static final Pattern CN_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5]+");

    /**
     * 判断某段文本是否是中文
     *
     * @param str 文本
     */
    public static boolean isChinese(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        Matcher m = CN_PATTERN.matcher(str);
        return m.find();
    }

    /**
     * 判断某个char 是否是英文字母
     *
     * @param c char
     */
    public static boolean isLetter(char c) {
        boolean f = c >= 65 && c <= 90;
        boolean s = c >= 97 && c <= 122;
        return f || s;
    }

    /**
     * 是否是大写
     * 
     * @param c
     */
    public static boolean isUppercase(char c) {
        return c >= 65 && c <= 90;
    }

    /**
     * 是否是小写
     * 
     * @param c
     */
    public static boolean isLowercase(char c) {
        return c >= 97 && c <= 122;
    }

    /** forbit instance */
    private CharacterUtil() {

    }
}
