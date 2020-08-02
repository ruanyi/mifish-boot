package com.ruanyi.mifish.common.utils;

/**
 * Description:
 *
 * https://stackoverflow.com/questions/28544867/is-it-ok-to-ignore-interruptedexception-if-nobody-calls-interrupt
 *
 * @author: ruanyi
 * @Date: 2020-07-26 12:31
 */
public final class ThreadUtils {

    /**
     * sleep
     *
     * @param mill
     */
    public static void sleep(long mill) {
        try {
            if (mill > 0) {
                Thread.sleep(mill);
            }
        } catch (InterruptedException e) {
            // Restore the interrupted status
            Thread.currentThread().interrupt();
        }
    }

    /**
     * getThreadName
     *
     * @return
     */
    public static String getThreadName() {
        return Thread.currentThread().getName();
    }

    /** forbit instance */
    private ThreadUtils() {}
}
