package com.ruanyi.mifish.kaproxy.exceptions;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2018-11-08 23:13
 */
public class NoSuchConsumerException extends Exception {

    /** group */
    private final String group;

    /** topic */
    private final String topic;

    /**
     * NoSuchConsumerException
     *
     * @param group
     * @param topic
     */
    public NoSuchConsumerException(String group, String topic) {
        super("");
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
