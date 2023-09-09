package com.ruanyi.mifish.kaproxy.consumer;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.Pair;

import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.kaproxy.ProcessorConsumer;
import com.ruanyi.mifish.kaproxy.model.ConsumerStatus;
import com.ruanyi.mifish.kernel.okhttp.MifishOkHttpMonitorInterceptor;
import com.ruanyi.mifish.kernel.okhttp.OkhttpMetricsEventListenerFactory;

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

    /**
     * @see ProcessorConsumer#start()
     */
    @Override
    public void start() throws Exception {
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

    /**
     * 做内部的启动动作
     *
     * @throws Exception
     */
    protected abstract void doInnerStart() throws Exception;

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
        try {
            this.stop(true);
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "AbstractOkHttpConsumer"), Pair.of("method", "asyncStop"),
                Pair.of("status", this.status), Pair.of("message", "The group consumer asyncStop error"));
        }
    }

    /**
     * @see ProcessorConsumer#syncStop()
     */
    @Override
    public void syncStop() throws Exception {
        this.stop(false);
    }

    /**
     * stop
     *
     * @param async
     * @throws Exception
     */
    protected abstract void stop(boolean async) throws Exception;

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
