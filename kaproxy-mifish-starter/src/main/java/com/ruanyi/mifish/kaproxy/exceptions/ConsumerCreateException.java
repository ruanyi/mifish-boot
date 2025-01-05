package com.ruanyi.mifish.kaproxy.exceptions;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2018-11-09 18:04
 */
public class ConsumerCreateException extends RuntimeException {

    /** group */
    private final String group;

    /** topic */
    private final String topic;

    /**
     * ConsumerCreateException
     * 
     * @param message
     * @param group
     * @param topic
     */
    public ConsumerCreateException(String message, String group, String topic) {
        super(message);
        this.group = group;
        this.topic = topic;
    }

    /**
     * ConsumerCreateException
     *
     * @param group
     * @param topic
     * @param cause
     */
    public ConsumerCreateException(String group, String topic, Throwable cause) {
        super(cause);
        this.group = group;
        this.topic = topic;
    }

    /**
     * getGroup
     *
     * @return
     */
    public final String getGroup() {
        return group;
    }

    /**
     * getTopic
     *
     * @return
     */
    public final String getTopic() {
        return topic;
    }

    /**
     * getMessage
     *
     * @return
     */
    @Override
    public String getMessage() {
        return "Consumer: group=" + this.group + ",topic=" + this.topic + " is not present";
    }
}
