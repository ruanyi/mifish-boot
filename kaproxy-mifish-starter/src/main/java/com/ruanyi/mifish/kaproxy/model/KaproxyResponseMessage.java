package com.ruanyi.mifish.kaproxy.model;

/**
 * Description:
 *
 * @author Yesphet
 * @date 10/08/2018
 */
public class KaproxyResponseMessage {

    /** key */
    private String key;

    /** offset */
    private long offset;

    /** partition */
    private long partition;

    /** topic */
    private String topic;

    /** value */
    private String value;

    /** error */
    private String error;

    /**
     * ConsumeResult
     * 
     * @param key
     * @param offset
     * @param partition
     * @param topic
     * @param value
     * @param error
     */
    public KaproxyResponseMessage(String key, long offset, long partition, String topic, String value, String error) {
        this.key = key;
        this.offset = offset;
        this.partition = partition;
        this.topic = topic;
        this.value = value;
        this.error = error;
    }

    // 这个方法只提供给client内部使用，使用方不需要用到。因为client响应给使用方的Reuslt一定是success的。
    // 这个是为了兼容Mqproxy的旧版，当队列中没消息时，会响应200，同时响应的body里有且只有一个error字段。
    // 新版mqproxy已去掉这种丑陋的东西。当队列中没消息时，响应204。
    public boolean isSuccessful() {
        return this.error == null || "".equals(this.error);
    }

    public String getKey() {
        return key;
    }

    public long getOffset() {
        return offset;
    }

    public long getPartition() {
        return partition;
    }

    public String getTopic() {
        return topic;
    }

    public String getValue() {
        return value;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return String.format("key: %s, value: %s, partition: %d, offset: %d, topic: %s", getKey(), getValue(),
            getPartition(), getOffset(), getTopic());
    }
}
