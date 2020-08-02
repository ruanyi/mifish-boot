package com.ruanyi.mifish.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2018-03-09 下午2:12
 */
public final class ThrowableUtil {

    /**
     * getStackTrace
     *
     * @param throwable
     * @return
     */
    public static String getStackTrace(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        pw.flush();
        pw.close();
        return sw.toString();
    }

    /**
     * ThrowableUtil
     */
    private ThrowableUtil() {

    }
}
