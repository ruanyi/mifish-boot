package com.ruanyi.mifish.kaproxy.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2017-12-05 18:09
 */
public class QueueMessage implements Serializable {

    private static final long serialVersionUID = -1264797147151455344L;

    public static final String MSG_KEY = "MSG_KEY";

    public static final String CLIENT_ID = "CLIENT_ID";

    public static final String OFFSET = "OFFSET";

    public static final String EN_QUEUE_TIME = "EN_QUEUE_TIME";

    public static final String MESSAGE_ID = "MESSAGE_ID";

    public static final String MSG_INVOKE_TS = "MSG_INVOKE_TS";

    public static final String MSG_RESOLVE_TS = "MSG_RESOLVE_TS";

    public static final String LOCAL_QUEUE_ELAPSE = "LOCAL_QUEUE_ELAPSE";

    /**
     * messageId：消息id 由生产者赋值：如果没有赋值，则为空 建议是：由生产端赋值，便于定位问题
     */
    private String messageId = "";

    /** group */
    private String group;

    /** topic */
    private String topic;

    /** partition */
    private long partition;

    /** message */
    private String message;

    /** attributes */
    private Map<String, Object> attributes = new HashMap<>();

    /** 消息被处理的次数 */
    private AtomicInteger invokedCount = new AtomicInteger(1);

    /** 出队列事件，由自动生成，在实例化该消息体时 */
    private final long dequeueTimeMillis = System.currentTimeMillis();

    /** 入队列事件，由生产者赋值 */
    private long enqueueTimeMillis = System.currentTimeMillis();

    /**
     * QueueMessage
     *
     * @param group
     * @param topic
     * @param partition
     * @param message
     */
    public QueueMessage(String group, String topic, long partition, String message) {
        this.group = group;
        this.topic = topic;
        this.partition = partition;
        this.message = message;
    }

    /**
     * getMessageId
     *
     * @return
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * setMessageId
     *
     * @param messageId
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * getGroup
     *
     * @return
     */
    public String getGroup() {
        return group;
    }

    /**
     * getTopic
     *
     * @return
     */
    public String getTopic() {
        return topic;
    }

    /**
     * getMessage
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * getPartition
     *
     * @return
     */
    public long getPartition() {
        return partition;
    }

    /**
     * addAttribute
     *
     * @param key
     * @param value
     */
    public void addAttribute(String key, Object value) {
        this.attributes.put(key, value);
    }

    /**
     * addAttributes
     *
     * @param attributes
     */
    public void addAttributes(Map<String, Object> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return;
        }
        this.attributes.putAll(attributes);
    }

    /**
     * getAttribute
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getAttribute(String key, Class<T> clazz) {
        Object obj = this.attributes.get(key);
        if (obj == null) {
            return null;
        }
        return clazz.cast(obj);
    }

    /**
     * getMsgKey
     *
     * @return
     */
    public String getMsgKey() {
        return this.getAttribute(MSG_KEY, String.class);
    }

    /**
     * getClientId
     *
     * @return
     */
    public String getClientId() {
        return this.getAttribute(CLIENT_ID, String.class);
    }

    /**
     * getClientId
     *
     * @return
     */
    public Long getOffset() {
        return this.getAttribute(OFFSET, Long.class);
    }

    /**
     * getInvokedCount
     *
     * @return
     */
    public int getInvokedCount() {
        return invokedCount.intValue();
    }

    /**
     * incInvokedCount
     *
     * @return
     */
    public int incInvokedCount() {
        return invokedCount.incrementAndGet();
    }

    /**
     * getDequeueTimeMillis
     *
     * @return
     */
    public long getDequeueTimeMillis() {
        return dequeueTimeMillis;
    }

    /**
     * getEnqueueTimeMillis
     *
     * @return
     */
    public long getEnqueueTimeMillis() {
        return enqueueTimeMillis;
    }

    /**
     * setEnqueueTimeMillis
     *
     * @param enqueueTimeMillis
     */
    public void setEnqueueTimeMillis(long enqueueTimeMillis) {
        this.enqueueTimeMillis = enqueueTimeMillis;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "QueueMessage{group=" + group + ", topic='" + topic + ", partition=" + partition + ", message='"
            + message + '}';
    }
}
