package com.ruanyi.mifish.kaproxy.model;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
public class AnnotationConsumerMeta implements Serializable {

    /** annotation */
    private Annotation annotation;

    /** topic */
    private String topic;

    /** consumer */
    private Object consumer;

    /** method */
    private Method method;

    /** MqproxyConsumerMeta */
    private AnnotationConsumerMeta() {

    }

    /**
     * getAnnotation
     * 
     * @return
     * @param <T>
     */
    public <T extends Annotation> T getAnnotation() {
        return (T)annotation;
    }

    /**
     * getMqproxyConsumer
     *
     * @return
     */
    public KaproxyConsumer getMqproxyConsumer() {
        return getAnnotation();
    }

    /**
     * getPoolName
     *
     * @return
     */
    public String getPoolName() {
        if (this.annotation instanceof KaproxyConsumer) {
            KaproxyConsumer kaproxyConsumer = getMqproxyConsumer();
            return kaproxyConsumer.poolName();
        }
        return null;
    }

    /**
     * getGroup
     *
     * @return
     */
    public String getGroup() {
        if (this.annotation instanceof KaproxyConsumer) {
            KaproxyConsumer kaproxyConsumer = getMqproxyConsumer();
            return kaproxyConsumer.group();
        }
        return null;
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
        return this.getPoolName() + ":" + getGroup() + ":" + getTopic();
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
     * @param annotation
     * @param topic
     * @param consumer
     * @param method
     * @return
     */
    public static AnnotationConsumerMeta of(Annotation annotation, String topic, Object consumer, Method method) {
        if (annotation == null || StringUtils.isBlank(topic) || consumer == null || method == null) {
            return null;
        }
        AnnotationConsumerMeta annotationConsumerMeta = new AnnotationConsumerMeta();
        annotationConsumerMeta.topic = topic;
        annotationConsumerMeta.annotation = annotation;
        annotationConsumerMeta.consumer = consumer;
        annotationConsumerMeta.method = method;
        return annotationConsumerMeta;
    }

    /**
     * parse2List
     *
     * @param annotation
     * @param topics
     * @param consumer
     * @param method
     * @return
     */
    public static List<AnnotationConsumerMeta> parse2List(Annotation annotation, Set<String> topics, Object consumer,
        Method method) {
        if (topics == null || topics.isEmpty()) {
            return Lists.newArrayList();
        }
        List<AnnotationConsumerMeta> annotationConsumerMetas = new ArrayList(topics.size());
        for (String topic : topics) {
            AnnotationConsumerMeta cm = AnnotationConsumerMeta.of(annotation, topic, consumer, method);
            if (cm != null) {
                annotationConsumerMetas.add(cm);
            }
        }
        return annotationConsumerMetas;
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
