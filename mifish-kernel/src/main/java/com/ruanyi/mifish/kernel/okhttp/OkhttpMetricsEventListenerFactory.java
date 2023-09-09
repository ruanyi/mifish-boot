package com.ruanyi.mifish.kernel.okhttp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

import io.micrometer.core.instrument.*;
import io.micrometer.core.lang.Nullable;
import okhttp3.*;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-09-09 21:02
 */
public class OkhttpMetricsEventListenerFactory implements EventListener.Factory {

    private final MeterRegistry registry;

    public OkhttpMetricsEventListenerFactory(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public EventListener create(Call call) {
        return new MetricsEventListener(registry);
    }

    public static class MetricsEventListener extends EventListener {
        CallState state = null;

        private final MeterRegistry registry;

        public MetricsEventListener(MeterRegistry registry) {
            this.registry = registry;
        }

        @Override
        public void callStart(Call call) {
            state = new CallState(registry.config().clock().monotonicTime());
        }

        @Override
        public void dnsStart(Call call, String domainName) {
            if (state != null) {
                state.dnsStart = registry.config().clock().monotonicTime();
            }
        }

        @Override
        public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
            if (state != null) {
                state.dnsEnd = registry.config().clock().monotonicTime();
            }
        }

        @Override
        public void secureConnectStart(Call call) {
            if (state != null) {
                state.sslStart = registry.config().clock().monotonicTime();
            }
        }

        @Override
        public void secureConnectEnd(Call call, Handshake handshake) {
            if (state != null) {
                state.sslEnd = registry.config().clock().monotonicTime();
            }
        }

        @Override
        public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
            if (state != null) {
                state.connStart = registry.config().clock().monotonicTime();
            }
        }

        @Override
        public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol,
            IOException ioe) {
            if (state != null) {
                state.connEnd = registry.config().clock().monotonicTime();
                state.connEx = ioe;
            }
        }

        @Override
        public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
            if (state != null) {
                state.connEnd = registry.config().clock().monotonicTime();
            }
        }

        @Override
        public void requestHeadersEnd(Call call, Request request) {
            if (state != null) {
                state.request = request;
                // 如果类似get这种没有body的请求，不会调用requestBodyEnd方法，所以需要在此处增加是时间设置
                state.sendEndTime = registry.config().clock().monotonicTime();
            }
        }

        @Override
        public void requestBodyEnd(Call call, long byteCount) {
            if (state != null) {
                state.sendEndTime = registry.config().clock().monotonicTime();
            }
        }

        @Override
        public void responseHeadersStart(Call call) {
            if (state != null) {
                state.firstPackageTime = registry.config().clock().monotonicTime();
            }
        }

        @Override
        public void callFailed(Call call, IOException e) {
            if (state != null) {
                state.exception = e;
                time(state);
            }
        }

        @Override
        public void responseHeadersEnd(Call call, Response response) {
            if (state != null) {
                state.response = response;
                time(state);
            }
        }

        /**
         * 处理事件
         *
         * @param state
         */
        private void time(CallState state) {

            String host = state.request != null ? state.request.url().host() : "UNKNOWN";
            Iterable<Tag> tags = Tags.of("host", host);
            Metrics.counter("service_http_cli_ext", tags).increment();

            // dns解析时间
            if (state.dnsStart > 0 && state.dnsEnd > 0) {
                DistributionSummary.builder("service_http_cli_ext_dns").tags(tags).description("Timer of OkHttp dns")
                    .register(registry).record(state.dnsEnd - state.startTime);
            }
            // ssl连接时间
            if (state.sslStart > 0 && state.sslEnd > 0) {
                DistributionSummary.builder("service_http_cli_ext_ssl").tags(tags).description("Timer of OkHttp ssl")
                    .register(registry).record(state.sslEnd - state.sslStart);
            }

            // 连接时间
            if (state.connStart > 0 && state.connEnd > 0) {
                DistributionSummary.builder("service_http_cli_ext_conn")
                    .tags(Tags.concat(tags, "type", state.connEx != null ? "true" : "false"))
                    .description("Timer of OkHttp connection").register(registry)
                    .record(state.connEnd - state.connStart);
            }

            // 请求的数据发送完成
            if (state.sendEndTime > 0) {
                DistributionSummary.builder("service_http_cli_ext_send")
                    .tags(Tags.concat(tags, "type", state.connEx != null ? "true" : "false"))
                    .description("Timer of OkHttp connection").register(registry)
                    .record(state.sendEndTime - state.startTime);
            }
            // 首包事件，调用方处理完数据返回的第一个数据包的时间
            if (state.firstPackageTime > 0) {
                DistributionSummary.builder("service_http_cli_ext_first_package")
                    .tags(Tags.concat(tags, "suc", state.connEx == null ? "true" : "false"))
                    .description("Timer of OkHttp connection").register(registry)
                    .record(state.firstPackageTime - state.startTime);
            }
        }

        private static class CallState {

            final long startTime;

            long dnsStart;
            long dnsEnd;

            long sslStart;
            long sslEnd; //

            long connStart; // 连接开始
            long connEnd;// 连接结束

            long sendEndTime; // 发送结束事件

            long firstPackageTime; // 首包时间

            @Nullable
            IOException connEx;

            @Nullable
            Request request;

            @Nullable
            Response response;

            @Nullable
            IOException exception;

            CallState(long startTime) {
                this.startTime = startTime;
            }
        }
    }
}
