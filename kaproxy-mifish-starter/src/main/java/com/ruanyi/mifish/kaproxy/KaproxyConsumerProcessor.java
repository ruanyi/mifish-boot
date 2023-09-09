package com.ruanyi.mifish.kaproxy;

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
import com.ruanyi.mifish.kaproxy.annotation.KaproxyConsumer;
import com.ruanyi.mifish.kaproxy.container.KaproxyConsumerMetaContainer;
import com.ruanyi.mifish.kaproxy.model.KaproxyConsumerMeta;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-11-17 15:26
 */
public class KaproxyConsumerProcessor implements BeanPostProcessor, PriorityOrdered, EnvironmentAware {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /** consumerMetaContainer */
    private KaproxyConsumerMetaContainer kaproxyConsumerMetaContainer = KaproxyConsumerMetaContainer.getInstance();

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
                KaproxyConsumer kaproxyConsumer = AnnotationUtils.findAnnotation(method, KaproxyConsumer.class);
                if (kaproxyConsumer != null) {
                    Set<String> topics = parseKafkaTopics(kaproxyConsumer);
                    this.kaproxyConsumerMetaContainer
                        .putConsumerMetas(KaproxyConsumerMeta.parse2List(kaproxyConsumer, topics, bean, method));
                    if (LOG.isInfoEnabled()) {
                        LOG.info(Pair.of("clazz", "KaproxyConsumerProcessor"),
                            Pair.of("method", "postProcessBeforeInitialization"), Pair.of("beanName", beanName),
                            Pair.of("methodName", method.getName()), Pair.of("group", kaproxyConsumer.group()),
                            Pair.of("config_topics", Arrays.toString(kaproxyConsumer.topics())),
                            Pair.of("config_topic_pattern", kaproxyConsumer.topicPattern()),
                            Pair.of("topics_values", topics), Pair.of("poolName", kaproxyConsumer.poolName()));
                    }
                }
            }
        }
        return bean;
    }

    /**
     * parseKafkaTopics
     *
     * @param kaproxyConsumer
     * @return
     */
    private Set<String> parseKafkaTopics(KaproxyConsumer kaproxyConsumer) {
        Set<String> result = Sets.newHashSet(kaproxyConsumer.topics());
        String propKey = kaproxyConsumer.topicPattern();
        String topicPath = null;
        if (StringUtils.isNotBlank(propKey)) {
            propKey = StringUtils.replace(propKey, "${", "");
            propKey = StringUtils.replace(propKey, "}", "");
            String envKey = StringUtils.upperCase(StringUtils.replace(propKey, ".", "_"));
            topicPath = System.getenv(envKey);
            if (StringUtils.isBlank(topicPath)) {
                topicPath = this.environment.getProperty(propKey);
            }
        }
        String[] configTopics = StringUtils.split(topicPath, ",");
        if (configTopics != null && configTopics.length > 0) {
            for (String ct : configTopics) {
                result.add(ct);
            }
        }
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
