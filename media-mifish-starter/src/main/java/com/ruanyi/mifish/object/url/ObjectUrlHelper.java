package com.ruanyi.mifish.object.url;

import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-09-02 18:24
 */
public final class ObjectUrlHelper {

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

    /** forbit init */
    private ObjectUrlHelper() {

    }
}
