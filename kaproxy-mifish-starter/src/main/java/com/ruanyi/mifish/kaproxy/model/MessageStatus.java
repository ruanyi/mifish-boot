package com.ruanyi.mifish.kaproxy.model;

/**
 * Description:
 * <p>
 * 消费的消费状态
 *
 * @author: ruanyi
 * @Date: 2017-12-06 10:37
 */
public enum MessageStatus {

    /**
     * 消息消费成功
     */
    SUCCESS,

    /**
     * 消息重试
     */
    RETRY,

    /**
     * 消息消费失败
     */
    FAILURE,

    /**
     * 系统异常，肯定是bug，需要紧急处理
     */
    SYS_EX;
}
