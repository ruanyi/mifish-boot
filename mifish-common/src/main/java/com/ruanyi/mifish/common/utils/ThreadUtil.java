package com.ruanyi.mifish.common.utils;

/**
 * Description:
 * <p>
 * https://stackoverflow.com/questions/28544867/is-it-ok-to-ignore-interruptedexception-if-nobody-calls-interrupt
 *
 * @author: ruanyi
 * @Date: 2017-12-05 20:26
 */
public final class ThreadUtil {

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

    /**
     * forbit instance
     */
    private ThreadUtil() {

    }
}
