package com.ruanyi.mifish.kaproxy.consumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.tuple.Pair;

import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.common.utils.JacksonUtils;
import com.ruanyi.mifish.kaproxy.ProcessorConsumer;
import com.ruanyi.mifish.kaproxy.message.MessageDigestEngine;
import com.ruanyi.mifish.kaproxy.model.ConsumerStatus;
import com.ruanyi.mifish.kaproxy.model.StartupConsumerMeta;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-11-17 16:09
 */
public class KaproxyOkHttpConsumer extends AbstractOkHttpConsumer {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /** startupConsumerMeta */
    private StartupConsumerMeta startupConsumerMeta;

    /** messageDigestEngine */
    private MessageDigestEngine messageDigestEngine;

    /**
     * 目前，一个StartupConsumerMeta 对应一个OkHttpConsumerTask 等后面有其他业务场景，再重新设计
     */
    private OkHttpConsumerTask okHttpConsumerTask;

    /** executors */
    private ExecutorService executors = null;

    /** lock */
    private Lock lock = new ReentrantLock();

    /**
     * KaproxyOkHttpConsumer
     *
     * @param startupConsumerMeta
     * @param messageDigestEngine
     */
    public KaproxyOkHttpConsumer(StartupConsumerMeta startupConsumerMeta, MessageDigestEngine messageDigestEngine) {
        this.startupConsumerMeta = startupConsumerMeta;
        this.messageDigestEngine = messageDigestEngine;
    }

    @Override
    public void doInnerStart() throws Exception {
        // 只有已经停止了的，才能启动
        if (!isStopped()) {
            return;
        }
        lock.lock();
        try {
            if (isStopped()) {
                long start = System.currentTimeMillis();
                // 启动和停止公用一个锁,如果前一个stop操作还没完成则会一直等待不会冲突
                this.status = ConsumerStatus.STARTING;
                // 启动线程池去消费
                this.executors = new ThreadPoolExecutor(1, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
                    (Runnable runnable) -> {
                        Thread thread = new Thread(runnable);
                        thread.setName("kafka-consumer-featcher-%d");
                        return thread;
                    });
                this.okHttpConsumerTask = new OkHttpConsumerTask(this.startupConsumerMeta, this.getOkHttpClient());
                this.okHttpConsumerTask.setMessageDigestEngine(this.messageDigestEngine);
                // 标记处于Running状态
                this.status = ConsumerStatus.RUNNING;
                if (LOG.isInfoEnabled()) {
                    LOG.info(Pair.of("clazz", "KaproxyProcessorConsumer"), Pair.of("method", "start"),
                        Pair.of("status", this.status),
                        Pair.of("startupConsumerMeta", JacksonUtils.toJSONString(this.startupConsumerMeta)),
                        Pair.of("cost", (System.currentTimeMillis() - start)),
                        Pair.of("message", "start the consumer successfully"));
                }
            }
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "KaproxyProcessorConsumer"), Pair.of("method", "start"),
                Pair.of("startupConsumerMeta", JacksonUtils.toJSONString(this.startupConsumerMeta)),
                Pair.of("status", this.status), Pair.of("message", "start the consumer failed"));
            // 停止该启动器
            this.stop(true);
            throw ex;
        } finally {
            lock.unlock();
        }
    }

    /**
     * stop
     *
     * @param async
     * @throws Exception
     */
    @Override
    public void stop(boolean async) throws Exception {
        if (isStopped() || isStopping()) {
            // 停止中或者已停止直接忽略
            return;
        }
        lock.lock();
        try {
            if (isStarting() || isRunning() || isReading()) {
                this.status = ConsumerStatus.STOPPING;
                if (async) {
                    new Thread(() -> {
                        stopReadKaproxy();
                        status = ConsumerStatus.STOPPED;
                    }).start();
                } else {
                    stopReadKaproxy();
                    status = ConsumerStatus.STOPPED;
                }
                if (LOG.isInfoEnabled()) {
                    LOG.info(Pair.of("clazz", "KaproxyOkHttpConsumer"), Pair.of("method", "stop"),
                        Pair.of("startupConsumerMeta", this.startupConsumerMeta.toString()),
                        Pair.of("status", this.status), Pair.of("async", async),
                        Pair.of("message", "Stoping the topic consumer"));
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * @see ProcessorConsumer#startRead()
     */
    @Override
    public boolean startRead() {
        // 已经是reading的状态了
        if (isReading()) {
            return true;
        }
        if (!isRunning()) {
            return false;
        }
        lock.lock();
        try {
            ConsumerStatus preStatus = this.status;
            // start kafka stream consumer task
            this.executors.submit(this.okHttpConsumerTask);
            this.status = ConsumerStatus.READING;
            LOG.warn(Pair.of("clazz", "KaproxyOkHttpConsumer"), Pair.of("method", "startRead"),
                Pair.of("startupConsumerMeta", JacksonUtils.toJSONString(this.startupConsumerMeta)),
                Pair.of("preStatus", preStatus), Pair.of("currentStatus", this.status));
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 停止读kaproxy
     *
     * @return
     */
    private void stopReadKaproxy() {
        // 停止读
        this.okHttpConsumerTask.stopRead();
        // 关闭线程池
        this.executors.shutdown();
        LOG.warn(Pair.of("clazz", "KaproxyOkHttpConsumer"), Pair.of("method", "stopReadKaproxy"),
            Pair.of("startupConsumerMeta", this.startupConsumerMeta.toString()), Pair.of("status", this.status),
            Pair.of("message", "stop read kafka stream success"));
    }
}
