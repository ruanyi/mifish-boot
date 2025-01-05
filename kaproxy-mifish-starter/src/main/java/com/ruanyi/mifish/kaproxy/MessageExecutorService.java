package com.ruanyi.mifish.kaproxy;

import com.ruanyi.mifish.kaproxy.message.MessageDigestEngine;
import com.ruanyi.mifish.kaproxy.model.QueueMessage;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-07 21:40
 */
public interface MessageExecutorService {

    /**
     * 提交消息任务
     *
     * @param message
     */
    void submit(QueueMessage message);

    /**
     * 获得消息消化引擎
     *
     * @return
     */
    MessageDigestEngine getMessageDigestEngine();

    /**
     * Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be
     * accepted. Invocation has no additional effect if already shut down.
     * <p>
     * <p>
     * This method does not wait for previously submitted tasks to complete execution.
     */
    void shutdown();

    /**
     * Returns {@code true} if this executor has been shut down.
     *
     * @return {@code true} if this executor has been shut down
     */
    boolean isShutdown();

    /**
     * Returns {@code true} if all tasks have completed following shut down. Note that {@code isTerminated} is never
     * {@code true} unless either {@code shutdown} or {@code shutdownNow} was called first.
     *
     * @return {@code true} if all tasks have completed following shut down
     */
    boolean isTerminated();

    /**
     * 处理中的任务数
     *
     * @return
     */
    int getProcessingTaskCount();

    /**
     * 等待中任务数量
     *
     * @return
     */
    int getWaitingTaskCount();

}
