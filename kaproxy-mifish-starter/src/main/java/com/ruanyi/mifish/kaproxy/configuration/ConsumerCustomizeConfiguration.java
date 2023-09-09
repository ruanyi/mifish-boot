package com.ruanyi.mifish.kaproxy.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ruanyi.mifish.kaproxy.ex.KaproxyConsumerExceptionResolver;
import com.ruanyi.mifish.kaproxy.message.ConsumerExceptionResolver;
import com.ruanyi.mifish.kaproxy.message.MessageHandler;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2019-03-29 13:48
 */
@Configuration
@AutoConfigureAfter({KaproxyConsumerConfiguration.class})
public class ConsumerCustomizeConfiguration {

    /**
     * newConsumerExceptionResolver
     *
     * @return
     */
    @Bean("consumerExceptionResolver")
    public ConsumerExceptionResolver newConsumerExceptionResolver(MessageHandler messageHandler) {
        ConsumerExceptionResolver consumerExceptionResolver = new KaproxyConsumerExceptionResolver();
        messageHandler.setConsumerExceptionResolver(consumerExceptionResolver);
        return consumerExceptionResolver;
    }
}
