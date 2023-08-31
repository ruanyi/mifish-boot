package com.ruanyi.mifish.tomcat.access;

import static com.ruanyi.mifish.common.utils.Constants.STRESS_TEST_FLAG;
import static com.ruanyi.mifish.common.utils.Constants.STRESS_TEST_FLAG_VALUE;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.AccessLog;
import org.apache.catalina.Globals;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.coyote.ActionCode;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.common.utils.DateUtil;
import com.ruanyi.mifish.common.utils.IPUtils;

/**
 * Description:
 * <p>
 * 自定义tomcat access log规范，一般情况下，不允许自定义，属于一体化监控的范畴
 *
 * @author: rls
 * @Date: 2018-02-05 13:46
 */
public class CustomAccessLogValve extends ValveBase implements AccessLog {

    /**
     * EXTERNAL_ACCESS_LOG_FIELDS
     */
    public static final String EXTERNAL_ACCESS_LOG_FIELDS = "external_access_log_fields";

    /**
     * KEY_ACCESSLOG_QUERYSTRING_ON
     */
    private static final String KEY_ACCESSLOG_QUERYSTRING_ON = "accesslog.querystring.on";

    /**
     * 一千
     */
    private static final BigDecimal ONE_THOUSAND = BigDecimal.valueOf(1000);

    /**
     * LOG
     */
    private static final MifishLogs LOG = MifishLogs.access;

    /**
     * Should this valve set request attributes for IP address, hostname, portal and port used for the request. Default
     * is <code>false</code>.
     *
     * @see #setRequestAttributesEnabled(boolean)
     */
    protected boolean requestAttributesEnabled = false;

    /**
     * enabled this component
     */
    protected boolean enabled = true;

    /**
     * LOG
     *
     * @param request
     * @param response
     * @param time
     */
    @Override
    public void log(Request request, Response response, long time) {
        if (!getState().isAvailable() || !getEnabled()) {
            return;
        }
        /**
         * XXX This is a bit silly, but we want to have start and stop time and duration consistent. It would be better
         * to keep start and stop simply in the request and/or response object and remove time (duration) from the
         * interface.
         */
        long start = request.getCoyoteRequest().getStartTime();
        Date date = new Date(start + time);
        if (LOG.isWarnEnabled()) {
            List<Pair<?, ?>> datas = new ArrayList<>(32);
            datas.add(Pair.of("@timestamp", getTimeISO8601(date)));
            datas.add(Pair.of("remote_addr", getRemoteIpAddress(request)));
            datas.add(Pair.of("server_addr", getLocalIpAddress(request)));
            datas.add(Pair.of("host", getHostString(request)));
            datas.add(Pair.of("request", getRequestString(request)));
            datas.add(Pair.of("request_method", getRequestMethod(request)));
            datas.add(Pair.of("request_uri", getRequestURI(request, response)));
            datas.add(Pair.of("request_protocol", getRequestProtocol(request)));
            datas.add(Pair.of("status", getStatusCode(request, response)));
            datas.add(Pair.of("first_byte_commit_time", getFirstByteCommitTime(request, response)));
            datas.add(Pair.of("request_time", getResponseTimeSencond(false, time)));
            datas.add(Pair.of("request_time_millis", getResponseTimeSencond(true, time)));
            datas.add(Pair.of("http_x_real_ip", getXRealIpString(request)));
            datas.add(Pair.of("http_x_forwarded_for", getXForwardedFor(request)));
            datas.add(Pair.of("content_length", getContentLength(request)));
            datas.add(Pair.of("sent_http_content_length", getReponseContentLengthHeader(response)));
            datas.add(Pair.of("body_bytes_sent", getBodyContentSend(request, response)));
            datas.add(Pair.of("http_cdn", getCdnString(request)));
            datas.add(Pair.of("access_token", getAccessTokenString(request)));
            datas.add(Pair.of("is_stress_test", getStressTestHeader(request)));
            datas.add(Pair.of("app_name", getAppNameString(request)));
            // 获取扩展字段，cannot be null
            datas.addAll(getExternalFieldsElement(request, response));
            LOG.warn(datas.toArray(new Pair<?, ?>[datas.size()]));
        }
    }

    /**
     * getRequestProtocol
     *
     * @param request
     * @return
     */
    private String getRequestProtocol(Request request) {
        if (request == null) {
            return "-";
        }
        String protocol = request.getProtocol();
        return StringUtils.isBlank(protocol) ? "-" : protocol;
    }

    /**
     * 假如发起的请求，并非本次框架处理的，则return "-"
     *
     * @param request
     * @param response
     * @return
     */
    private String getRequestURI(Request request, Response response) {
        if (request == null || response == null) {
            return "-";
        }
        String requestURI = request.getRequestURI();
        if (response.isError()) {
            // Check for connection aborted cond
            Throwable ex = (Throwable)request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
            if (ex instanceof NoHandlerFoundException) {
                requestURI = "-";
            }
        }
        // if status is 404,return requestURI = "-"，避免监控报表里有一坨的乱七八糟的东西
        int status = response.getStatus();
        if (status == 404) {
            requestURI = "-";
        }
        return StringUtils.isBlank(requestURI) ? "-" : requestURI;
    }

    /**
     * getRequestMethod
     *
     * @param request
     * @return
     */
    private String getRequestMethod(Request request) {
        if (request == null) {
            return "-";
        }
        String method = StringUtils.upperCase(request.getMethod());
        HttpMethod httpMethod = HttpMethod.resolve(method);
        return (httpMethod == null) ? "-" : method;
    }

    /**
     * 获得压测标志位
     *
     * @param request
     * @return
     */
    private boolean getStressTestHeader(Request request) {
        HttpServletRequest httpServletRequest = request.getRequest();
        String parameter = httpServletRequest.getHeader(STRESS_TEST_FLAG);
        return StringUtils.equals(parameter, STRESS_TEST_FLAG_VALUE);
    }

    /**
     * getExternalFieldsElement
     *
     * @param request
     * @param response
     * @return
     */
    private List<Pair<?, ?>> getExternalFieldsElement(Request request, Response response) {
        Map<String, Object> fields = (Map<String, Object>)request.getRequest().getAttribute(EXTERNAL_ACCESS_LOG_FIELDS);
        if (fields != null && !fields.isEmpty()) {
            List<Pair<?, ?>> extendDatas = new ArrayList<>(fields.size());
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                extendDatas
                    .add(Pair.of(entry.getKey(), (entry.getValue() != null ? String.valueOf(entry.getValue()) : "-")));
            }
            return extendDatas;
        }
        return new ArrayList<>(0);
    }

    /**
     * getAccessTokenString
     *
     * @param request
     * @return
     */
    private String getAccessTokenString(Request request) {
        return request.getHeader("Access-Token");
    }

    /**
     * http header中的cdn。Tomcat配置： %{cdn}i ； NGINX配置：$http_cdn 。
     *
     * @param request
     * @return
     */
    private String getCdnString(Request request) {
        return request.getHeader("cdn");
    }

    private String getAppNameString(Request request) {
        if (request == null) {
            return "-";
        }
        String appName = request.getHeader("app_name");
        return StringUtils.isBlank(appName) ? "-" : appName;
    }

    /**
     * 实际发送的长度。Tomcat配置：%b ， NGINX配置：$body_bytes_sent
     *
     * @param request
     * @param response
     * @return
     */
    private String getBodyContentSend(Request request, Response response) {
        if (request == null || response == null) {
            return "-";
        }
        // Don't need to flush since trigger for LOG message is after the
        // response has been committed
        long length = response.getBytesWritten(false);
        if (length <= 0) {
            // Protect against nulls and unexpected types as these values
            // may be set by untrusted applications
            Object start = request.getAttribute(Globals.SENDFILE_FILE_START_ATTR);
            if (start instanceof Long) {
                Object end = request.getAttribute(Globals.SENDFILE_FILE_END_ATTR);
                if (end instanceof Long) {
                    length = ((Long)end).longValue() - ((Long)start).longValue();
                }
            }
        }
        if (length <= 0) {
            return "-";
        } else {
            return Long.toString(length);
        }
    }

    /**
     * http response header中的content-length。 Tomcat配置：%{content-length}o ； NGINX配置：$sent_http_content_length
     *
     * @param response
     * @return
     */
    private String getReponseContentLengthHeader(Response response) {
        if (response == null) {
            return "-";
        }
        return Integer.toString(response.getContentLength());
    }

    /**
     * //http header 中的请求长度。 Tomcat配置： %{content-length}i NGINX配置： $content_length
     *
     * @param request
     * @return
     */
    private String getContentLength(Request request) {
        return Integer.toString(request.getContentLength());
    }

    /**
     * http header 中的 X-Forwarded-For。 Tomcat配置：%{X-Forwarded-For}i ； NGINX配置：$http_x_forwarded_for。
     *
     * @param request
     * @return
     */
    private String getXForwardedFor(Request request) {
        return request.getHeader("X-Forwarded-For");
    }

    /**
     * http header中的X-Real-IP，当前约定。Tomcat配置：%{X-Real-IP}i ； NGINX配置：$http_x_real_ip。
     *
     * @param request
     * @return
     */
    private String getXRealIpString(Request request) {
        return request.getHeader("X-Real-IP");
    }

    /**
     * 响应时间。 Tomcat配置: %T NGINX配置：$request_time 。 因为nginx是以秒为单位，所以后端也统一成秒。
     * <p>
     * 默认是3位小数，数值型，方便查询过滤。默认值 0 。
     *
     * @param millis 是否使用毫秒表示
     * @param time 毫秒表示的时间
     * @return
     */
    private double getResponseTimeSencond(boolean millis, long time) {
        if (millis) {
            return time;
        }
        BigDecimal bdTime = BigDecimal.valueOf(time);
        BigDecimal result = bdTime.divide(ONE_THOUSAND);
        return result.doubleValue();
    }

    /**
     * //输出第一个字节的处理时间，通常就是后端的处理时间。 Tomcat配置：%F ； NGINX配置：没有找到对应的字段，~~但是 $upstream_header_time 这个字段含义比较接近，就使用这个字段。~~
     * Tengine 2.1.2 目前还不支持这个字段，暂时还是用 - 代替。
     *
     * @param request
     * @param response
     * @return
     */
    private long getFirstByteCommitTime(Request request, Response response) {
        if (request == null || response == null) {
            return -1;
        }
        long commitTime = response.getCoyoteResponse().getCommitTime();
        if (commitTime < 0) {
            return commitTime;
        } else {
            long delta = commitTime - request.getCoyoteRequest().getStartTime();
            return delta;
        }
    }

    /**
     * getStatusCode
     *
     * @param request
     * @param response
     * @return
     */
    private int getStatusCode(Request request, Response response) {
        if (request == null || response == null) {
            return 0;
        }
        // if status is 500,change to 499 in sometime
        int status = response.getStatus();
        if (status == 500) {
            // Check whether connection IO is in "not allowed" state
            AtomicBoolean isIoAllowed = new AtomicBoolean(false);
            request.getCoyoteRequest().action(ActionCode.IS_IO_ALLOWED, isIoAllowed);
            if (!isIoAllowed.get()) {
                status = 499;
            } else if (response.isError()) {
                // Check for connection aborted cond
                Throwable ex = (Throwable)request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
                if (ex instanceof ClientAbortException) {
                    status = 499;
                }
            }
        }
        return status;
    }

    /**
     * 第一行的请求内容，比如 "GET /57dc04ae886836082.mp4 HTTP/1.1" 。 Tomcat 配置："%r" NGINX配置："$request"
     *
     * @param request
     * @return
     */
    private String getRequestString(Request request) {
        if (request == null) {
            return "-";
        }
        StringBuilder buf = new StringBuilder();
        String method = request.getMethod();
        if (method == null) {
            // No method means no request line
            buf.append('-');
        } else {
            buf.append(request.getMethod());
            buf.append(' ');
            buf.append(request.getRequestURI());
            buf.append(getQueryString(request));
            buf.append(' ');
            buf.append(request.getProtocol());
        }
        return buf.toString();
    }

    /**
     * getQueryString
     *
     * @param request
     * @return
     */
    private String getQueryString(Request request) {
        // 如果是压测流量，则不打印query_string
        // query_string，通过开关进行控制，默认是打开状态的
        if (request == null || request.getQueryString() == null || getStressTestHeader(request)) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        buf.append('?');
        buf.append(request.getQueryString());
        return buf.toString();
    }

    /**
     * 请求的HOST。 Tomcat配置：%{host}i ；NGINX配置：$host
     *
     * @return
     */
    private String getHostString(Request request) {
        return request.getHeader("HOST");
    }

    /**
     * ISO8601标准的日志时间，格式 yyyy-MM-dd''HH:mm:ss.SSSZ ，对应的Tomcat配置：%{yyyy-MM-dd'T'HH:mm:ss.SSSZ}t NGINX配置：$time_iso8601
     *
     * @param date
     * @return
     */
    private String getTimeISO8601(Date date) {
        return DateUtil.dateToString(date, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }

    /**
     * 本地监听地址。 Tomcat配置：%A:%p ; NGINX配置：$server_addr（加这个字段，主要是为了方便处理当不同的服务节点可能在一个日志文件里面的情况)
     *
     * @param request
     * @return
     */
    private String getLocalIpAddress(Request request) {
        if (request == null) {
            return "-";
        }
        Object port = request.getAttribute(SERVER_PORT_ATTRIBUTE);
        if (requestAttributesEnabled && port != null) {
            return IPUtils.getLocalIp() + ":" + port.toString();
        }
        return IPUtils.getLocalIp() + ":" + Integer.toString(request.getServerPort());
    }

    /**
     * 远程IP地址。 对应的Tomcat配置：%a NGINX配置：$remote_addr
     *
     * @param request
     * @return
     */
    private String getRemoteIpAddress(Request request) {
        Object addr = request.getAttribute(REMOTE_ADDR_ATTRIBUTE);
        if (requestAttributesEnabled && addr != null) {
            return addr.toString();
        }
        return request.getRemoteAddr();
    }

    /**
     * @return the enabled flag.
     */
    public boolean getEnabled() {
        return enabled;
    }

    /**
     * setEnabled
     *
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * setRequestAttributesEnabled
     *
     * @param requestAttributesEnabled
     */
    @Override
    public void setRequestAttributesEnabled(boolean requestAttributesEnabled) {
        this.requestAttributesEnabled = requestAttributesEnabled;
    }

    /**
     * setRequestAttributesEnabled
     *
     * @return
     */
    @Override
    public boolean getRequestAttributesEnabled() {
        return this.requestAttributesEnabled;
    }

    /**
     * invoke
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        getNext().invoke(request, response);
    }
}
