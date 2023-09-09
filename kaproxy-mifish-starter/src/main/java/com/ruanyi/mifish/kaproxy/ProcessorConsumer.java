package com.ruanyi.mifish.kaproxy;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-11-17 16:05
 */
public interface ProcessorConsumer {

    /**
     * start
     *
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * asyncStop
     */
    void asyncStop();

    /**
     * syncStop
     *
     * @throws Exception
     */
    void syncStop() throws Exception;

    /**
     * 启动，占据partition，触发消费消费
     *
     * @return
     */
    boolean startRead();

    /**
     * isStopped
     *
     * @return
     */
    boolean isStopped();

    /**
     * isRunning
     *
     * @return
     */
    boolean isRunning();

    /**
     * isStarting
     *
     * @return
     */
    boolean isStarting();

    /**
     * isStopping
     *
     * @return
     */
    boolean isStopping();

    /**
     * isReading
     *
     * @return
     */
    boolean isReading();
}
