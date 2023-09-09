package com.ruanyi.mifish.kernel.okhttp;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;

import io.micrometer.core.instrument.Metrics;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2020-09-04 20:13
 */
public abstract class AbstractOkHttpClient implements InitializingBean {

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

    /**
     * @see InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.okHttpClient = new OkHttpClient.Builder()
            .connectionPool(
                new ConnectionPool(this.maxIdleConnections, this.connctionKeepAliveMinutes, TimeUnit.MINUTES))
            .connectTimeout(this.connectTimeout, TimeUnit.MILLISECONDS)
            .readTimeout(this.readTimeout, TimeUnit.MILLISECONDS)
            .addInterceptor(new MifishOkHttpMonitorInterceptor(this.defaultFireTime))
            .eventListenerFactory(new OkhttpMetricsEventListenerFactory(Metrics.globalRegistry)).build();
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
