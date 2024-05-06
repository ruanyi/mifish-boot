package com.ruanyi.mifish.url;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.BitSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.ruanyi.mifish.common.logs.MifishLogs;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-09-02 18:24
 */
public final class ObjectUrlHelper {

    private static final MifishLogs LOG = MifishLogs.media;

    /**
     * 只获得存粹的key
     *
     * @param url
     * @return
     */
    public static String parseLastKey(String url) {
        // 把问号以后的一坨字符串都舍去掉
        String temp = StringUtils.substringBefore(url, "?");
        return StringUtils.substringAfterLast(temp, "/");
    }

    /**
     * parseFileSuffix
     *
     * @param url
     * @return
     */
    public static String parseFileSuffix(String url) {
        // 把问号以后的一坨字符串都舍去掉
        String temp = StringUtils.substringBefore(url, "?");
        return StringUtils.substringAfterLast(temp, ".");
    }

    private static BitSet DONT_NEED_ENCODING;

    static {
        DONT_NEED_ENCODING = new BitSet(128);
        int i;
        for (i = 'a'; i <= 'z'; i++) {
            DONT_NEED_ENCODING.set(i);
        }
        for (i = 'A'; i <= 'Z'; i++) {
            DONT_NEED_ENCODING.set(i);
        }
        for (i = '0'; i <= '9'; i++) {
            DONT_NEED_ENCODING.set(i);
        }
        DONT_NEED_ENCODING.set('+');
        DONT_NEED_ENCODING.set('-');
        DONT_NEED_ENCODING.set('_');
        DONT_NEED_ENCODING.set('.');
        DONT_NEED_ENCODING.set('*');
        DONT_NEED_ENCODING.set('%');
    }

    /**
     * 字符串是否经过了url encode
     *
     * @param text 字符串
     * @return true表示是
     */
    public static boolean hasEnCode(String text) {
        if (StringUtils.isBlank(text)) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            int c = text.charAt(i);
            if (!DONT_NEED_ENCODING.get(c)) {
                return false;
            }
            if (c == '%' && (i + 2) < text.length()) {
                // 判断是否符合urlEncode规范
                char c1 = text.charAt(++i);
                char c2 = text.charAt(++i);
                if (!isDigit16Char(c1) || !isDigit16Char(c2)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 对字符串s用charset方式编码
     *
     * @param s 需要编码的字符串
     * @return 编码后的字符串
     */
    public static String encode(String s) {
        return encode(s, "UTF-8");
    }

    /**
     * 对字符串s用charset方式编码
     *
     * @param s 需要编码的字符串
     * @param charset 编码类型
     * @return 编码后的字符串
     */
    public static String encode(String s, String charset) {
        if (StringUtils.isBlank(s)) {
            return null;
        }
        try {
            return URLEncoder.encode(s, charset);
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "ObjectUrlHelper"), Pair.of("method", "encode"), Pair.of("str", s),
                Pair.of("charset", charset), Pair.of("msg", "encode str error"));
            return null;
        }
    }

    /**
     * 对已编码的字符串解码
     *
     * @param s 字符串
     * @return 解码后字符串
     */
    public static String decode(String s) {
        return decode(s, "UTF-8");
    }

    /**
     * 对已编码的字符串解码
     *
     * @param s 字符串
     * @param charset 解码方式
     * @return 解码后字符串
     */
    public static String decode(String s, String charset) {
        try {
            return URLDecoder.decode(s, charset);
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "ObjectUrlHelper"), Pair.of("method", "decode"), Pair.of("str", s),
                Pair.of("charset", charset), Pair.of("msg", "decode str error"));
            return null;
        }
    }

    /**
     * 判断c是否是16进制的字符
     *
     * @param c 字符
     * @return true表示是
     */
    private static boolean isDigit16Char(char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F');
    }

    /**
     * 需持续优化
     *
     * @param url
     * @return
     */
    public static boolean isValidUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return false;
        }
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            // ignore
            return false;
        }
        if (uri.getHost() == null) {
            return false;
        }
        if (uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https")) {
            return true;
        }
        return false;
    }

    /** forbit init */
    private ObjectUrlHelper() {

    }
}
