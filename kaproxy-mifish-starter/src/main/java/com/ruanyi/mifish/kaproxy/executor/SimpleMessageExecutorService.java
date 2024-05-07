package com.ruanyi.mifish.kaproxy.executor;

import java.util.concurrent.ThreadPoolExecutor;

import com.ruanyi.mifish.kaproxy.MessageExecutorService;
import com.ruanyi.mifish.kaproxy.annotation.KaproxyConsumer;
import com.ruanyi.mifish.kaproxy.container.KaproxyConsumerMetaContainer;
import com.ruanyi.mifish.kaproxy.message.MessageDigestEngine;
import com.ruanyi.mifish.kaproxy.model.QueueMessage;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-07 21:43
 */
public class SimpleMessageExecutorService implements MessageExecutorService {

    /** messageDigestEngine */
    private MessageDigestEngine messageDigestEngine;

    /** threadPoolExecutor */
    private ThreadPoolExecutor threadPoolExecutor = null;

    /**
     * SimpleMessageExecutorService
     * 
     * @param messageDigestEngine
     * @param threadPoolExecutor
     */
    public SimpleMessageExecutorService(MessageDigestEngine messageDigestEngine,
        ThreadPoolExecutor threadPoolExecutor) {
        this.messageDigestEngine = messageDigestEngine;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    /**
     * @see MessageExecutorService#submit(QueueMessage)
     */
    @Override
    public void submit(final QueueMessage message) {
        final KaproxyConsumer kaproxyConsumer =
            KaproxyConsumerMetaContainer.getInstance().getKaproxyConsumer(message.getGroup(), message.getTopic());
        threadPoolExecutor.submit(() -> {
            // 触发业务处理
            this.messageDigestEngine.digest(kaproxyConsumer, message);
        });
    }

    /**
     * @see MessageExecutorService#getMessageDigestEngine()
     */
    @Override
    public MessageDigestEngine getMessageDigestEngine() {
        return this.messageDigestEngine;
    }

    /**
     * @see MessageExecutorService#shutdown()
     */
    @Override
    public void shutdown() {
        this.threadPoolExecutor.shutdown();
    }

    /**
     * @see MessageExecutorService#isShutdown()
     */
    @Override
    public boolean isShutdown() {
        return this.threadPoolExecutor.isShutdown();
    }

    /**
     * @see MessageExecutorService#isTerminated()
     */
    @Override
    public boolean isTerminated() {
        return this.threadPoolExecutor.isTerminated();
    }

    /**
     * @see MessageExecutorService#getProcessingTaskCount()
     */
    @Override
    public int getProcessingTaskCount() {
        return this.threadPoolExecutor.getActiveCount();
    }

    /**
     * @see MessageExecutorService#getWaitingTaskCount()
     */
    @Override
    public int getWaitingTaskCount() {
        return this.threadPoolExecutor.getQueue().size();
    }
}
