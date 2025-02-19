package com.ruanyi.mifish.kaproxy;

import java.util.List;

import org.springframework.context.Lifecycle;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-11-17 16:16
 */
public interface ProcessorConsumerContainer extends Lifecycle {

    /**
     * getAllQueueConsumers
     *
     * @return
     */
    List<ProcessorConsumer> getAllConsumers();

    /**
     * getKaproxyRequestMessageFactory
     * 
     * @return
     */
    KaproxyRequestMessageFactory getKaproxyRequestMessageFactory();
}
