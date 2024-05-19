package com.ruanyi.mifish.mqproxy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.util.ReflectionUtils;

import com.google.common.collect.Sets;
import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.mqproxy.annotation.MqproxyConsumer;
import com.ruanyi.mifish.mqproxy.container.MqproxyConsumerMetaContainer;
import com.ruanyi.mifish.mqproxy.model.AnnotationConsumerMeta;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-11-17 15:26
 */
public class MqproxyConsumerProcessor implements BeanPostProcessor, PriorityOrdered, EnvironmentAware {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /** consumerMetaContainer */
    private MqproxyConsumerMetaContainer mqProxyConsumerMetaContainer = MqproxyConsumerMetaContainer.getInstance();

    /** 动态获取配置信息 */
    private Environment environment;

    /**
     * @see BeanPostProcessor#postProcessBeforeInitialization(Object, String)
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        if (methods != null) {
            for (Method method : methods) {
                // 1、处理MqproxyConsumer
                MqproxyConsumer mqproxyConsumer = AnnotationUtils.findAnnotation(method, MqproxyConsumer.class);
                if (mqproxyConsumer != null) {
                    Set<String> topics = parseMqproxyTopics(mqproxyConsumer);
                    this.mqProxyConsumerMetaContainer
                        .putConsumerMetas(AnnotationConsumerMeta.parse2List(mqproxyConsumer, topics, bean, method));
                    if (LOG.isInfoEnabled()) {
                        LOG.info(Pair.of("clazz", "MqproxyConsumerProcessor"),
                            Pair.of("method", "postProcessBeforeInitialization"), Pair.of("beanName", beanName),
                            Pair.of("methodName", method.getName()), Pair.of("group", mqproxyConsumer.group()),
                            Pair.of("config_topics", Arrays.toString(mqproxyConsumer.topics())),
                            Pair.of("config_topic_pattern", mqproxyConsumer.topicPattern()),
                            Pair.of("topics_values", topics), Pair.of("poolName", mqproxyConsumer.poolName()));
                    }
                }
            }
        }
        return bean;
    }

    /**
     * parseFromEnvironment
     * 
     * @param topicPattern
     * @return
     */
    private Set<String> parseFromEnvironment(String topicPattern) {
        if (StringUtils.isNotBlank(topicPattern)) {
            topicPattern = StringUtils.replace(topicPattern, "${", "");
            topicPattern = StringUtils.replace(topicPattern, "}", "");
            String envKey = StringUtils.upperCase(StringUtils.replace(topicPattern, ".", "_"));
            String topicPath = System.getenv(envKey);
            if (StringUtils.isBlank(topicPath)) {
                topicPath = this.environment.getProperty(topicPattern);
            }
            String[] configTopics = StringUtils.split(topicPath, ",");
            if (configTopics != null && configTopics.length > 0) {
                return Sets.newHashSet(configTopics);
            }
        }
        return Sets.newHashSet();
    }

    /**
     * parseMqproxyTopics
     *
     * @param mqproxyConsumer
     * @return
     */
    private Set<String> parseMqproxyTopics(MqproxyConsumer mqproxyConsumer) {
        Set<String> result = Sets.newHashSet(mqproxyConsumer.topics());
        result.addAll(parseFromEnvironment(mqproxyConsumer.topicPattern()));
        return result;
    }

    /**
     * @see BeanPostProcessor#postProcessAfterInitialization(Object, String)
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * @see EnvironmentAware#setEnvironment(Environment)
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * @see Ordered@getOrder()
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
