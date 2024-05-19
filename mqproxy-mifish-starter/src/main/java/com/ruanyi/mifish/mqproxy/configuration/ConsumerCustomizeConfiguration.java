package com.ruanyi.mifish.mqproxy.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ruanyi.mifish.mqproxy.message.ConsumerExceptionResolver;
import com.ruanyi.mifish.mqproxy.message.MessageHandler;
import com.ruanyi.mifish.mqproxy.resolver.MqproxyConsumerExceptionResolver;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2019-03-29 13:48
 */
@Configuration
@AutoConfigureAfter({MqproxyConsumerConfiguration.class})
public class ConsumerCustomizeConfiguration {

    /**
     * newConsumerExceptionResolver
     *
     * @return
     */
    @Bean("consumerExceptionResolver")
    public ConsumerExceptionResolver newConsumerExceptionResolver(MessageHandler messageHandler) {
        ConsumerExceptionResolver consumerExceptionResolver = new MqproxyConsumerExceptionResolver();
        messageHandler.setConsumerExceptionResolver(consumerExceptionResolver);
        return consumerExceptionResolver;
    }
}
