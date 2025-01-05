package com.ruanyi.mifish.kaproxy.configuration;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.ruanyi.mifish.common.utils.NumberUtil;
import com.ruanyi.mifish.kaproxy.KaproxyConsumerProcessor;
import com.ruanyi.mifish.kaproxy.KaproxyRequestMessageFactory;
import com.ruanyi.mifish.kaproxy.MessageExecutorService;
import com.ruanyi.mifish.kaproxy.ProcessorConsumerContainer;
import com.ruanyi.mifish.kaproxy.container.KaproxyProcessorConsumerContainer;
import com.ruanyi.mifish.kaproxy.executor.SimpleMessageExecutorService;
import com.ruanyi.mifish.kaproxy.factory.SimpleKaproxyRequestMessageFactory;
import com.ruanyi.mifish.kaproxy.message.MessageDigestEngine;
import com.ruanyi.mifish.kaproxy.message.MessageHandler;
import com.ruanyi.mifish.kaproxy.message.engine.SimpleMessageDigestEngine;
import com.ruanyi.mifish.kaproxy.message.handler.SimpleMessageHandler;
import com.ruanyi.mifish.kaproxy.message.nodes.RequestContextNode;
import com.ruanyi.mifish.kaproxy.model.KaproxyStartupMeta;
import com.ruanyi.mifish.kaproxy.shutdown.ProcessorGracefulShutdown;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-11-17 15:23
 */
@Configuration
@AutoConfigureAfter({PropertyPlaceholderAutoConfiguration.class})
public class KaproxyConsumerConfiguration implements EnvironmentAware {

    /** environment */
    private Environment environment;

    /**
     * newStartupConsumerMetaFactory
     *
     * @return
     */
    @Bean
    public KaproxyRequestMessageFactory newMqproxyRequestMessageFactory() {
        return new SimpleKaproxyRequestMessageFactory();
    }

    /**
     * 一般情况下，单实例启动
     * <p>
     * 其中,start方法，由：MqproxyConsumerProcessor，基于事件去启动 stop方法，由优雅关闭去关闭
     *
     * @param kaproxyStartupMeta
     * @param kaproxyRequestMessageFactory
     * @param messageExecutorService
     * @return
     */
    @Bean(name = "queueConsumerContainer")
    public ProcessorConsumerContainer newQueueConsumerContainer(final KaproxyStartupMeta kaproxyStartupMeta,
                                                                KaproxyRequestMessageFactory kaproxyRequestMessageFactory, MessageExecutorService messageExecutorService) {
        KaproxyProcessorConsumerContainer mqproxyConsumerContainer = new KaproxyProcessorConsumerContainer(
                kaproxyStartupMeta, kaproxyRequestMessageFactory, messageExecutorService);
        return mqproxyConsumerContainer;
    }

    /**
     * 构建 mq proxy 启动的配置信息
     *
     * @return
     */
    @Bean
    public KaproxyStartupMeta newMqproxyStartupMeta() {
        String effectGroupTopics = obtainEffectGroupTopics();
        int consumeConcurrency = obtainConsumeConcurrency();
        int consumptionRate = obtainConsumptionRate();
        int handleConcurrency = obtainHandleConcurrency();
        KaproxyStartupMeta kaproxyStartupMeta =
            KaproxyStartupMeta.of(effectGroupTopics, consumeConcurrency, consumptionRate, handleConcurrency);
        return kaproxyStartupMeta;
    }

    /**
     * obtainHandleConcurrency
     *
     * @return
     */
    private int obtainHandleConcurrency() {
        String str =
            obtainStaticConfigurationValue("MQ_HANDLE_CONCURRENCY", "mifish.processor.mqproxy.handle.concurrency");
        return NumberUtil.isInteger(str) ? Integer.parseInt(str) : 2;
    }

    /**
     * obtainStaticConfigurationValue
     * 
     * @param envKey
     * @param propKey
     * @return
     */
    private String obtainStaticConfigurationValue(String envKey, String propKey) {
        String config = System.getenv(envKey);
        if (StringUtils.isBlank(config)) {
            config = System.getProperty(envKey);
        }
        if (StringUtils.isBlank(config) && StringUtils.isNotEmpty(propKey)) {
            config = this.environment.getProperty(propKey);
        }
        return config;
    }

    /**
     * 默认值：-1，代表无限制
     *
     * @return
     */
    private int obtainConsumptionRate() {
        String str = obtainStaticConfigurationValue("MQ_CONSUMPTION_RATE", "mifish.processor.mqproxy.consume.rate");
        return NumberUtil.isInteger(str) ? Integer.parseInt(str) : -1;
    }

    /**
     * obtainConsumeConcurrency
     *
     * @return
     */
    private int obtainConsumeConcurrency() {
        String str =
            obtainStaticConfigurationValue("MQ_CONSUME_CONCURRENCY", "mifish.processor.mqproxy.consume.concurrency");
        return NumberUtil.isInteger(str) ? Integer.parseInt(str) : 1;
    }

    /**
     * 方案跟：spring.profiles.active一模一样 <br>
     * <p>
     * 1、优先从系统的环境变量获取，
     * <p>
     * 2、如果没有，则从系统属性中获取<br>
     * <p>
     * 3、如果还是没有，则从配置文件中获取。
     *
     * @return
     */
    private String obtainEffectGroupTopics() {
        String str =
            obtainStaticConfigurationValue("MQ_EFFECT_GROUP_TOPICS", "mifish.processor.mqproxy.effect-group-topics");
        return str;
    }

    /**
     * newMessageDigestEngine
     *
     * @param messageHandler
     * @return
     */
    @Bean
    public MessageDigestEngine newMessageDigestEngine(MessageHandler messageHandler) {
        return new SimpleMessageDigestEngine(messageHandler);
    }

    /**
     * newMessageExecutorService
     *
     * @param kaproxyStartupMeta
     * @param messageDigestEngine
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    public MessageExecutorService newMessageExecutorService(KaproxyStartupMeta kaproxyStartupMeta,
                                                            MessageDigestEngine messageDigestEngine) {
        int handleConcurrency = kaproxyStartupMeta.getHandleConcurrency();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(handleConcurrency, handleConcurrency, 60L,
            TimeUnit.SECONDS, new SynchronousQueue<>());
        SimpleMessageExecutorService multiQueuedMessageExecutorService =
            new SimpleMessageExecutorService(messageDigestEngine, threadPoolExecutor);
        return multiQueuedMessageExecutorService;
    }

    /**
     * newMessageHandler
     *
     * @return
     */
    @Bean
    public MessageHandler newMessageHandler() {
        return new SimpleMessageHandler();
    }

    /**
     * newRequestContextNode
     *
     * @return
     */
    @Bean("requestContextNode")
    public RequestContextNode newRequestContextNode() {
        return new RequestContextNode(-10);
    }

    /**
     * newXiuxiuConsumerProcessor
     * <p>
     * 同样的问题
     * <p>
     * https://stackoverflow.com/questions/49489050/spring-boot-2-0-value-does-not-work-in-configration
     * <p>
     * 切记：必须是：static
     *
     * @return
     */
    @Bean("mqproxyConsumerProcessor")
    public static KaproxyConsumerProcessor newMqproxyConsumerProcessor() {
        return new KaproxyConsumerProcessor();
    }

    /**
     * processorGracefulShutdown
     *
     * @param processorConsumerContainer
     * @return
     */
    @Bean
    public ProcessorGracefulShutdown
        newProcessorGracefulShutdown(ProcessorConsumerContainer processorConsumerContainer) {
        return new ProcessorGracefulShutdown(processorConsumerContainer);
    }

    /**
     * @see EnvironmentAware#setEnvironment(Environment)
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
