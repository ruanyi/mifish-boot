package com.ruanyi.mifish.kaproxy.model;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.ruanyi.mifish.kaproxy.annotation.KaproxyConsumer;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-11-17 15:17
 */
public class KaproxyConsumerMeta implements Serializable {

    /** kaproxyConsumer */
    private KaproxyConsumer kaproxyConsumer;

    /** topic */
    private String topic;

    /** consumer */
    private Object consumer;

    /** method */
    private Method method;

    /** KaproxyConsumerMeta */
    private KaproxyConsumerMeta() {

    }

    /**
     * getKaproxyConsumer
     *
     * @return
     */
    public KaproxyConsumer getKaproxyConsumer() {
        return this.kaproxyConsumer;
    }

    /**
     * getPoolName
     *
     * @return
     */
    public String getPoolName() {
        return this.kaproxyConsumer.poolName();
    }

    /**
     * getGroup
     *
     * @return
     */
    public String getGroup() {
        return this.kaproxyConsumer.group();
    }

    /**
     * getTopic
     *
     * @return
     */
    public String getTopic() {
        return this.topic;
    }

    /**
     * getKey
     *
     * @return
     */
    public String getKey() {
        return this.getGroup() + ":" + this.getTopic();
    }

    /**
     * toString
     *
     * @return
     */
    @Override
    public String toString() {
        return this.getPoolName() + ":" + getGroup() + ":" + Arrays.toString(this.kaproxyConsumer.topics());
    }

    /**
     * getConsumer
     *
     * @return
     */
    public Object getConsumer() {
        return consumer;
    }

    /**
     * getMethod
     *
     * @return
     */
    public Method getMethod() {
        return method;
    }

    /**
     * of
     *
     * @param kaproxyConsumer
     * @param topic
     * @param consumer
     * @param method
     * @return
     */
    public static KaproxyConsumerMeta of(KaproxyConsumer kaproxyConsumer, String topic, Object consumer,
        Method method) {
        if (kaproxyConsumer == null || StringUtils.isBlank(topic) || consumer == null || method == null) {
            return null;
        }
        KaproxyConsumerMeta kaproxyConsumerMeta = new KaproxyConsumerMeta();
        kaproxyConsumerMeta.topic = topic;
        kaproxyConsumerMeta.kaproxyConsumer = kaproxyConsumer;
        kaproxyConsumerMeta.consumer = consumer;
        kaproxyConsumerMeta.method = method;
        return kaproxyConsumerMeta;
    }

    /**
     * parse2List
     *
     * @param kaproxyConsumer
     * @param topics
     * @param consumer
     * @param method
     * @return
     */
    public static List<KaproxyConsumerMeta> parse2List(KaproxyConsumer kaproxyConsumer, Set<String> topics,
        Object consumer, Method method) {
        if (topics == null || topics.isEmpty()) {
            return Lists.newArrayList();
        }
        List<KaproxyConsumerMeta> xiuxiuKafkaConsumerMetas = new ArrayList(topics.size());
        for (String topic : topics) {
            KaproxyConsumerMeta cm = KaproxyConsumerMeta.of(kaproxyConsumer, topic, consumer, method);
            if (cm != null) {
                xiuxiuKafkaConsumerMetas.add(cm);
            }
        }
        return xiuxiuKafkaConsumerMetas;
    }

    /**
     * buildKey
     *
     * @param group
     * @param topic
     * @return
     */
    public static String buildKey(String group, String topic) {
        return group + ":" + topic;
    }
}
