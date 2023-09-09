package com.ruanyi.mifish.kaproxy.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ruanyi.mifish.kaproxy.KaproxyConsumerProcessor;
import com.ruanyi.mifish.kaproxy.ProcessorConsumerContainer;
import com.ruanyi.mifish.kaproxy.StartupConsumerMetaFactory;
import com.ruanyi.mifish.kaproxy.container.KaproxyProcessorConsumerContainer;
import com.ruanyi.mifish.kaproxy.factory.SimpleStartupConsumerMetaFactory;
import com.ruanyi.mifish.kaproxy.message.MessageDigestEngine;
import com.ruanyi.mifish.kaproxy.message.MessageHandler;
import com.ruanyi.mifish.kaproxy.message.engine.SimpleMessageDigestEngine;
import com.ruanyi.mifish.kaproxy.message.handler.SimpleMessageHandler;
import com.ruanyi.mifish.kaproxy.message.nodes.RequestContextNode;
import com.ruanyi.mifish.kaproxy.shutdown.ProcessorGracefulShutdown;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-11-17 15:23
 */
@Configuration
@AutoConfigureAfter({PropertyPlaceholderAutoConfiguration.class})
public class KaproxyConsumerConfiguration {

    /** effectGroupTopics */
    @Value("${mifish.processor.kaproxy.effect-group-topics}")
    public String effectGroupTopics;

    /**
     * newStartupConsumerMetaFactory
     *
     * @return
     */
    @Bean
    public StartupConsumerMetaFactory newStartupConsumerMetaFactory() {
        return new SimpleStartupConsumerMetaFactory();
    }

    /**
     * 一般情况下，单实例启动
     * 
     * 其中,start方法，由：KaproxyConsumerProcessor，基于事件去启动 stop方法，由优雅关闭去关闭
     *
     * @return
     */
    @Bean(name = "queueConsumerContainer")
    public ProcessorConsumerContainer newQueueConsumerContainer(StartupConsumerMetaFactory startupConsumerMetaFactory,
        MessageDigestEngine messageDigestEngine) {
        String effectGroupTopics = obtainEffectGroupTopics();
        KaproxyProcessorConsumerContainer xiuxiuQueueConsumerContainer =
            new KaproxyProcessorConsumerContainer(effectGroupTopics, startupConsumerMetaFactory, messageDigestEngine);
        return xiuxiuQueueConsumerContainer;
    }

    /**
     * 方案跟：spring.profiles.active一模一样 <br>
     * <p>
     * 优先从系统的环境变量获取，如果没有，则从系统属性中获取<br>
     * <p>
     * 如果还是没有，则从配置文件中获取
     *
     * @return
     */
    private String obtainEffectGroupTopics() {
        String effectGroupTopics = System.getenv("KAFKA_EFFECT_GROUP_TOPICS");
        if (StringUtils.isBlank(effectGroupTopics)) {
            effectGroupTopics = System.getProperty("KAFKA_EFFECT_GROUP_TOPICS");
        }
        if (StringUtils.isBlank(effectGroupTopics)) {
            effectGroupTopics = this.effectGroupTopics;
        }
        return effectGroupTopics;
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
    @Bean("kaproxyConsumerProcessor")
    public static KaproxyConsumerProcessor newKaproxyConsumerProcessor() {
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
}
