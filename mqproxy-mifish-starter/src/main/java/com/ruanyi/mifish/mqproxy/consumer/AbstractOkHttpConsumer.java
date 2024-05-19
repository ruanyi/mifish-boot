package com.ruanyi.mifish.mqproxy.consumer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.tuple.Pair;

import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.kernel.okhttp.MifishOkHttpMonitorInterceptor;
import com.ruanyi.mifish.kernel.okhttp.OkhttpMetricsEventListenerFactory;
import com.ruanyi.mifish.mqproxy.ProcessorConsumer;
import com.ruanyi.mifish.mqproxy.model.ConsumerStatus;

import io.micrometer.core.instrument.Metrics;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-11-18 14:58
 */
public abstract class AbstractOkHttpConsumer implements ProcessorConsumer {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /** okHttpClient 发起http请求 */
    private OkHttpClient okHttpClient = null;

    /** connectTimeout，单位：毫秒 */
    private long connectTimeout = 60 * 1000;

    /** readTimeout，单位：毫秒 */
    private long readTimeout = 120 * 1000;

    /** maxIdleConnections： */
    private int maxIdleConnections = 10;

    /** connctionKeepAliveMinutes： */
    private int connctionKeepAliveMinutes = 10;

    /** 默认的，超时时间，打印 */
    private int defaultFireTime = 1000;

    /** status */
    protected ConsumerStatus status = ConsumerStatus.STOPPED;

    /** lock */
    private Lock lock = new ReentrantLock();

    /**
     * @see ProcessorConsumer#start()
     */
    @Override
    public void start() throws Exception {
        // 只有已经停止了的，才能启动
        if (!isStopped()) {
            return;
        }
        lock.lock();
        try {
            if (isStopped()) {
                this.okHttpClient = new OkHttpClient.Builder()
                    .connectionPool(
                        new ConnectionPool(this.maxIdleConnections, this.connctionKeepAliveMinutes, TimeUnit.MINUTES))
                    .connectTimeout(this.connectTimeout, TimeUnit.MILLISECONDS)
                    .readTimeout(this.readTimeout, TimeUnit.MILLISECONDS)
                    .addInterceptor(new MifishOkHttpMonitorInterceptor(this.defaultFireTime))
                    .eventListenerFactory(new OkhttpMetricsEventListenerFactory(Metrics.globalRegistry)).build();
                // 做内部的启动动作
                doInnerStart();
            }
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", this.getClass().getSimpleName()), Pair.of("method", "start"),
                Pair.of("status", this.status), Pair.of("message", "start the consumer failed"));
            // 停止该启动器
            this.doInnerStop(true);
            throw ex;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 做内部的启动动作
     *
     * @throws Exception
     */
    protected abstract void doInnerStart() throws Exception;

    /**
     * doInnerStop
     *
     * @param async
     * @throws Exception
     */
    protected abstract void doInnerStop(boolean async) throws Exception;

    /**
     * isStopped
     *
     * @return
     */
    @Override
    public boolean isStopped() {
        return ConsumerStatus.STOPPED == this.status;
    }

    /**
     * @see ProcessorConsumer#asyncStop()
     */
    @Override
    public void asyncStop() {
        if (isStopped() || isStopping()) {
            // 停止中或者已停止直接忽略
            return;
        }
        lock.lock();
        try {
            if (isStarting() || isRunning() || isReading()) {
                this.status = ConsumerStatus.STOPPING;
                this.doInnerStop(true);
            }
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", this.getClass().getSimpleName()), Pair.of("method", "asyncStop"),
                Pair.of("status", this.status), Pair.of("message", "The group consumer asyncStop error"));
        } finally {
            lock.unlock();
        }
    }

    /**
     * @see ProcessorConsumer#syncStop()
     */
    @Override
    public void syncStop() throws Exception {
        if (isStopped() || isStopping()) {
            // 停止中或者已停止直接忽略
            return;
        }
        this.doInnerStop(false);
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
            doInnerStartRead();
            this.status = ConsumerStatus.READING;
            LOG.warn(Pair.of("clazz", this.getClass().getSimpleName()), Pair.of("method", "startRead"),
                Pair.of("preStatus", preStatus), Pair.of("currentStatus", this.status));
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * doInnerStartRead
     */
    protected abstract void doInnerStartRead();

    /**
     * isReading
     *
     * @return
     */
    @Override
    public boolean isReading() {
        return ConsumerStatus.READING == this.status;
    }

    /**
     * isStopping
     *
     * @return
     */
    @Override
    public boolean isStopping() {
        return ConsumerStatus.STOPPING == this.status;
    }

    /**
     * isRunning
     *
     * @return
     */
    @Override
    public boolean isRunning() {
        return ConsumerStatus.RUNNING == this.status;
    }

    /**
     * isStarting
     *
     * @return
     */
    @Override
    public boolean isStarting() {
        return ConsumerStatus.STARTING == this.status;
    }

    /**
     * getOkHttpClient
     *
     * @return
     */
    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * setConnectTimeout
     *
     * @param connectTimeout
     */
    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * setReadTimeout
     *
     * @param readTimeout
     */
    public void setReadTimeout(long readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * setMaxIdleConnections
     *
     * @param maxIdleConnections
     */
    public void setMaxIdleConnections(int maxIdleConnections) {
        this.maxIdleConnections = maxIdleConnections;
    }

    /**
     * setConnctionKeepAliveMinutes
     *
     * @param connctionKeepAliveMinutes
     */
    public void setConnctionKeepAliveMinutes(int connctionKeepAliveMinutes) {
        this.connctionKeepAliveMinutes = connctionKeepAliveMinutes;
    }

    /**
     * setDefaultFireTime
     *
     * @param defaultFireTime
     */
    public void setDefaultFireTime(int defaultFireTime) {
        this.defaultFireTime = defaultFireTime;
    }
}
