package com.ruanyi.mifish.kaproxy;

import java.util.Set;

import org.springframework.context.Lifecycle;

import com.ruanyi.mifish.kaproxy.model.GroupTopic;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-11-17 16:16
 */
public interface ProcessorConsumerContainer extends Lifecycle {

    /**
     * getProcessorConsumer
     *
     * @param groupTopic
     * @return
     */
    ProcessorConsumer getProcessorConsumer(GroupTopic groupTopic);

    /**
     * isContainConsumer
     *
     * @param groupTopic
     * @return
     */
    boolean isContainConsumer(GroupTopic groupTopic);

    /**
     * addProcessorConsumer
     *
     * @param groupTopic
     * @param processorConsumer
     * @return
     */
    ProcessorConsumerContainer addProcessorConsumer(GroupTopic groupTopic, ProcessorConsumer processorConsumer);

    /**
     * getAllQueueConsumers
     *
     * @return
     */
    Set<ProcessorConsumerContainer> getAllConsumers();
}
