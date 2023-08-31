package com.ruanyi.mifish.tomcat.tomcat;

import org.apache.catalina.connector.Connector;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import com.ruanyi.mifish.common.utils.NumberUtil;

/**
 * Description:
 * <p>
 * 类似：org.springframework.boot.autoconfigure.web.ServerProperties
 * <p>
 * 不过，暂不打算通过：通过注解：ConfigurationProperties，而是通过代码写死的方式
 *
 * @author: rls
 * @Date: 2018-08-30 16:46
 */
public class Tomcat9ConnectorCustomizer implements TomcatConnectorCustomizer, EnvironmentAware {

    /** environment */
    private Environment environment;

    /**
     * customize
     *
     * @param connector
     */
    @Override
    public void customize(Connector connector) {
        Http11NioProtocol protocol = (Http11NioProtocol)connector.getProtocolHandler();
        // 之前配置在server.xml里的相关的值
        protocol.setConnectionTimeout(getProtocolConnectionTimeout());
        protocol.setAcceptCount(getProtocolAcceptCount());
        protocol.setMaxKeepAliveRequests(getProtocolMaxKeepAliveRequests());
        protocol.setMaxConnections(getProtocolMaxConnections());
        // set max http header size
        protocol.setMaxHttpHeaderSize(getProtocolMaxHttpHeaderSize());
        // fix bug，这边有坑：目前客户端里传过来的url里的ab code里的值：包含：{} []等特殊字符，会报以下问题：
        // Invalid character found in the request target. The validation characters are defined in RFC 7230 and RFC 3986
        // 通过这种方式进行过滤，看tomcat里的代码
        protocol.setRelaxedQueryChars(getProtocolRelaxedQueryChars());
    }

    /**
     * 默认是false</br>
     * 如果需要配置，则在环境变量中配置：MIFISH_PRESTART_MINSPARE_THREADS，或者，配置文件中配置：MIFISH.prestart.minspare.threads
     *
     * @return
     */
    private boolean getPrestartminSpareThreads() {
        String prestartThreadsStr = System.getenv("MIFISH_PRESTART_MINSPARE_THREADS");
        if (StringUtils.isBlank(prestartThreadsStr)) {
            prestartThreadsStr = this.environment.getProperty("MIFISH.prestart.minspare.threads");
        }
        return Boolean.parseBoolean(prestartThreadsStr);
    }

    /**
     * getProtocolConnectionTimeout
     *
     * @return
     */
    private int getProtocolConnectionTimeout() {
        String timeoutStr = System.getenv("MIFISH_PROTOCOL_CONNECTION_TIMEOUT");
        if (StringUtils.isBlank(timeoutStr)) {
            timeoutStr = environment.getProperty("mifish.protocol.connection.timeout");
        }
        if (NumberUtil.isInteger(timeoutStr)) {
            return Integer.parseInt(timeoutStr);
        }
        return 90000;
    }

    /**
     * getProtocolAcceptCount
     *
     * @return
     */
    private int getProtocolAcceptCount() {
        String acceptCountStr = System.getenv("MIFISH_PROTOCOL_ACCEPT_COUNT");
        if (StringUtils.isBlank(acceptCountStr)) {
            acceptCountStr = this.environment.getProperty("mifish.protocol.accept.count");
        }
        if (NumberUtil.isInteger(acceptCountStr)) {
            return Integer.parseInt(acceptCountStr);
        }
        return 10000;
    }

    /**
     * getProtocolMaxKeepAliveRequests
     *
     * @return
     */
    private int getProtocolMaxKeepAliveRequests() {
        String maxKeepAliveRequestsStr = System.getenv("MIFISH_PROTOCOL_MAX_KEEP_ALIVE_REQUESTS");
        if (StringUtils.isBlank(maxKeepAliveRequestsStr)) {
            maxKeepAliveRequestsStr = this.environment.getProperty("mifish.protocol.max.keep.alive.count");
        }
        if (NumberUtil.isInteger(maxKeepAliveRequestsStr)) {
            return Integer.parseInt(maxKeepAliveRequestsStr);
        }
        return -1;
    }

    /**
     * getProtocolMaxConnections
     *
     * @return
     */
    private int getProtocolMaxConnections() {
        String maxConnectionsStr = System.getenv("MIFISH_PROTOCOL_MAX_CONNECTIONS");
        if (StringUtils.isBlank(maxConnectionsStr)) {
            maxConnectionsStr = this.environment.getProperty("mifish.protocol.max.connections");
        }
        if (NumberUtil.isInteger(maxConnectionsStr)) {
            return Integer.parseInt(maxConnectionsStr);
        }
        return 10000;
    }

    /**
     * getProtocolMaxHttpHeaderSize
     *
     * @return
     */
    private int getProtocolMaxHttpHeaderSize() {
        String maxHttpHeaderSizeStr = System.getenv("MIFISH_PROTOCOL_MAX_HTTP_HEADER_SIZE");
        if (StringUtils.isBlank(maxHttpHeaderSizeStr)) {
            maxHttpHeaderSizeStr = this.environment.getProperty("mifish.protocol.max.http.header.size");
        }
        if (NumberUtil.isInteger(maxHttpHeaderSizeStr)) {
            return Integer.parseInt(maxHttpHeaderSizeStr);
        }
        return 81920;
    }

    /**
     * getProtocolRelaxedQueryChars
     *
     * @return
     */
    private String getProtocolRelaxedQueryChars() {
        String relaxedQueryChars = System.getenv("MIFISH_PROTOCOL_RELAXED_QUERY_CHARS");
        if (StringUtils.isBlank(relaxedQueryChars)) {
            relaxedQueryChars = this.environment.getProperty("mifish.protocol.relaxed.query.chars");
        }
        return StringUtils.isBlank(relaxedQueryChars) ? "^{}[]|&quot;" : relaxedQueryChars;
    }

    /**
     * getTomcatThreadPoolMinSpareThreads
     *
     * @return
     */
    private int getTomcatThreadPoolMinSpareThreads() {
        String theadPoolMinSpareThreadsStr = System.getenv("MIFISH_TOMCAT_THREADPOOL_MIN_SPARE_THREADS");
        if (StringUtils.isBlank(theadPoolMinSpareThreadsStr)) {
            theadPoolMinSpareThreadsStr = this.environment.getProperty("mifish.tomcat.threadpool.min.spare.threads");
        }
        if (NumberUtil.isInteger(theadPoolMinSpareThreadsStr)) {
            return Integer.parseInt(theadPoolMinSpareThreadsStr);
        }
        return 300;
    }

    /**
     * setEnvironment
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
