package com.ruanyi.mifish.mqproxy.shutdown;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.mqproxy.ProcessorConsumerContainer;

/**
 * Description:
 *
 * @依赖于 GracefulShutdownHook 发出来的优雅关闭事件
 * @author: ruanyi
 * @Date: 2018-09-05 11:02
 */
public class ProcessorGracefulShutdown implements ApplicationListener<ContextClosedEvent> {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /** processorConsumerContainer */
    private ProcessorConsumerContainer processorConsumerContainer;

    /**
     * ProcessorGracefulShutdown
     *
     * @param processorConsumerContainer
     */
    public ProcessorGracefulShutdown(ProcessorConsumerContainer processorConsumerContainer) {
        this.processorConsumerContainer = processorConsumerContainer;
    }

    /**
     * @see ApplicationListener#onApplicationEvent(ApplicationEvent)
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        try {
            this.processorConsumerContainer.stop();
            if (LOG.isInfoEnabled()) {
                LOG.info(Pair.of("clazz", "ProcessorGracefulShutdown"), Pair.of("method", "onApplicationEvent"),
                    Pair.of("message", "shut down processor consumer success"));
            }
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "ProcessorGracefulShutdown"), Pair.of("method", "onApplicationEvent"),
                Pair.of("message", "shut down processor consumer occur unknow exception"));
        }
    }

    /**
     * getProcessorConsumerContainer
     *
     * @return
     */
    public ProcessorConsumerContainer getProcessorConsumerContainer() {
        return processorConsumerContainer;
    }
}
