package com.ruanyi.mifish.mqproxy.model;

/**
 * Description:
 * <p>
 * 消费者状态
 *
 * @author: ruanyi
 * @Date: 2017-12-05 22:01
 */
public enum ConsumerStatus {

    /**
     * STARTING
     */
    STARTING,

    /**
     * RUNNING
     */
    RUNNING,

    /**
     * READING
     */
    READING,

    /**
     * STOPPING
     */
    STOPPING,

    /**
     * STOPPED
     */
    STOPPED
}
