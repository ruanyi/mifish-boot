package com.ruanyi.mifish.kernel.okhttp;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.lang3.tuple.Pair;

import com.ruanyi.mifish.common.logs.MifishLogs;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2021-08-11 16:15
 */
public class MifishOkHttpMonitorInterceptor implements Interceptor {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /** 默认的告警时间 */
    private static final int DEFAULT_HTTP_FIRE_TIME = 1000;

    private static final String DEFAULT_APP_NAME = "mifish";

    /** appName */
    private String appName = DEFAULT_APP_NAME;

    /** fireTime */
    private int defaultFireTime = DEFAULT_HTTP_FIRE_TIME;

    /**
     * MeituOkHttpCostInterceptor
     *
     * @param defaultFireTime
     */
    public MifishOkHttpMonitorInterceptor(int defaultFireTime) {
        this(DEFAULT_APP_NAME, defaultFireTime);
    }

    /**
     * MeituOkHttpCostInterceptor
     *
     * @param appName
     */
    public MifishOkHttpMonitorInterceptor(String appName) {
        this(appName, DEFAULT_HTTP_FIRE_TIME);
    }

    /**
     * MeituOkHttpCostInterceptor
     * 
     * @param appName
     * @param defaultFireTime
     */
    public MifishOkHttpMonitorInterceptor(String appName, int defaultFireTime) {
        this.appName = appName;
        this.defaultFireTime = defaultFireTime;
    }

    /**
     * @see Interceptor#intercept(Chain)
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        long start = System.currentTimeMillis();
        Request request = null;
        Response response = null;
        try {
            Request.Builder builder = chain.request().newBuilder();
            request = builder.addHeader("app_name", this.appName).build();
            response = chain.proceed(request);
            return response;
        } finally {
            onFinally(request, response, start);
        }
    }

    /**
     * 打印相关日志，绝对，绝对，绝对不允许跑出异常
     * 
     * @param request
     * @param response
     * @param start
     */
    private void onFinally(Request request, Response response, long start) {
        try {
            if (request == null) {
                return;
            }
            long cost = System.currentTimeMillis() - start;
            URL url = request.url().url();
            if (isLogHttpFireMsg(cost, url.getHost())) {
                LOG.warn(Pair.of("clazz", "MeituOkHttpMonitorInterceptor"), Pair.of("method", "onFinally"),
                    Pair.of("protocol", url.getProtocol()), Pair.of("host", url.getHost()),
                    Pair.of("port", url.getPort()), Pair.of("query_string", url.getQuery()), Pair.of("cost", cost),
                    Pair.of("resp_http_code", response == null ? -1 : response.code()));
            }
        } catch (Exception ex) {
            // ingore
        }
    }

    /**
     * isLogHttpFireMsg
     * 
     * @param cost
     * @param host
     * @return
     */
    private boolean isLogHttpFireMsg(long cost, String host) {
        return cost >= this.defaultFireTime;
    }
}
