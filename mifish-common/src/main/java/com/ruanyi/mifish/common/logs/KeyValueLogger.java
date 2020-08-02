package com.ruanyi.mifish.common.logs;

import com.ruanyi.mifish.common.context.RequestContext;
import com.ruanyi.mifish.common.utils.JackJsonUtil;
import com.ruanyi.mifish.common.utils.ThrowableUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.ruanyi.mifish.common.utils.ThreadUtils.getThreadName;

/**
 * Description: com.mifish.common.logs.KeyValueLogger
 * <p>
 * 提供key value的日志风格，所有业务组件的日志均实现该接口，进行日志打印
 * <p>
 * 它只是一个工具而已，规范必须使用key value风格
 * <p>
 * 第一：日志格式：json化</br>
 * 第二：大部分日志打印到stdout里，指定日志除外</br>
 * 第三：所有业务组件的日志，均不需要单独进行log4j2配置，由各个业务组件固定写死即可：约定大于配置</br>
 * 第四：压测情况下，不打trace、debug、info日志</br>
 * 第五：KeyValueLogger固定使用log4j2来打印日志，默认实现容器化的：json化标准，暂不支持其他打印日志的方式（有没有必要，有待考量）</br>
 *
 * @author: rls
 * @Date: 2019-01-21
 */
public interface KeyValueLogger {

    String ACCESS = "access";

    /**
     * getCategory
     *
     * @return
     */
    String getCategory();

    /**
     * getLogger
     *
     * @return
     */
    Logger getLogger();

    /**
     * 向log4j2注册相应的logger，假如log4j2.xml已经存在了，则使用业务方配置好的
     *
     * @param logger
     */
    static void register(Logger logger) {
        MifishLoggerContainer.getInstance().addLogger(logger);
    }

    /**
     * isTraceEnabled
     *
     * @return
     */
    default boolean isTraceEnabled() {
        return this.getLogger().isTraceEnabled();
    }

    /**
     * isDebugEnabled
     *
     * @return
     */
    default boolean isDebugEnabled() {
        return this.getLogger().isDebugEnabled();
    }

    /**
     * isInfoEnabled
     *
     * @return
     */
    default boolean isInfoEnabled() {
        return this.getLogger().isInfoEnabled();
    }

    /**
     * isWarnEnabled
     *
     * @return
     */
    default boolean isWarnEnabled() {
        return this.getLogger().isWarnEnabled();
    }

    /**
     * trace
     *
     * @param pairs
     */
    default void trace(Pair<?, ?>... pairs) {
        if (isTraceEnabled()) {
            Map<Object, Object> params = new HashMap<>(pairs.length + 4);
            if (!StringUtils.equals(getCategory(), ACCESS)) {
                params.put("level", "trace");
            }
            params.put("category", getCategory());
            params.put("requestId", RequestContext.getCurrentRequestId());
            params.put("threadName", getThreadName());
            for (Pair<?, ?> p : pairs) {
                if (p != null) {
                    params.put(p.getLeft(), p.getRight());
                }
            }
            getLogger().trace("{}", JackJsonUtil.toJSONString(params));
        }
    }

    /**
     * debug
     *
     * @param pairs
     */
    default void debug(Pair<?, ?>... pairs) {
        if (isDebugEnabled()) {
            Map<Object, Object> params = new HashMap<>(pairs.length + 4);
            if (!StringUtils.equals(getCategory(), ACCESS)) {
                params.put("level", "debug");
            }
            params.put("category", getCategory());
            params.put("requestId", RequestContext.getCurrentRequestId());
            params.put("threadName", getThreadName());
            for (Pair<?, ?> p : pairs) {
                if (p != null) {
                    params.put(p.getLeft(), p.getRight());
                }
            }
            getLogger().debug("{}", JackJsonUtil.toJSONString(params));
        }
    }

    /**
     * debug
     *
     * @param t
     * @param pairs
     */
    default void debug(Throwable t, Pair<?, ?>... pairs) {
        if (isDebugEnabled()) {
            Map<Object, Object> params = new HashMap<>(pairs.length + 4);
            if (!StringUtils.equals(getCategory(), ACCESS)) {
                params.put("level", "debug");
            }
            params.put("category", getCategory());
            params.put("requestId", RequestContext.getCurrentRequestId());
            params.put("threadName", getThreadName());
            for (Pair<?, ?> p : pairs) {
                if (p != null) {
                    params.put(p.getLeft(), p.getRight());
                }
            }
            params.put("stackTrace", ThrowableUtil.getStackTrace(t));
            getLogger().debug("{}", JackJsonUtil.toJSONString(params));
        }
    }

    /**
     * info
     *
     * @param pairs
     */
    default void info(Pair<?, ?>... pairs) {
        if (isInfoEnabled()) {
            Map<Object, Object> params = new HashMap<>(pairs.length + 4);
            if (!StringUtils.equals(getCategory(), ACCESS)) {
                params.put("level", "info");
            }
            params.put("category", getCategory());
            params.put("requestId", RequestContext.getCurrentRequestId());
            params.put("threadName", getThreadName());
            for (Pair<?, ?> p : pairs) {
                if (p != null) {
                    params.put(p.getLeft(), p.getRight());
                }
            }
            getLogger().info("{}", JackJsonUtil.toJSONString(params));
        }
    }

    /**
     * warn
     *
     * @param pairs
     */
    default void warn(Pair<?, ?>... pairs) {
        if (isWarnEnabled()) {
            Map<Object, Object> params = new HashMap<>(pairs.length + 4);
            params.put("category", getCategory());
            params.put("requestId", RequestContext.getCurrentRequestId());
            if (!StringUtils.equals(getCategory(), ACCESS)) {
                params.put("level", "warn");
            }
            params.put("threadName", getThreadName());
            for (Pair<?, ?> p : pairs) {
                if (p != null) {
                    params.put(p.getLeft(), p.getRight());
                }
            }
            getLogger().warn("{}", JackJsonUtil.toJSONString(params));
        }
    }

    /**
     * warn
     *
     * @param t
     * @param pairs
     */
    default void warn(Throwable t, Pair<?, ?>... pairs) {
        if (isWarnEnabled()) {
            Map<Object, Object> params = new HashMap<>(pairs.length + 5);
            if (!StringUtils.equals(getCategory(), ACCESS)) {
                params.put("level", "warn");
            }
            params.put("category", getCategory());
            params.put("requestId", RequestContext.getCurrentRequestId());
            params.put("threadName", getThreadName());
            for (Pair<?, ?> p : pairs) {
                if (p != null) {
                    params.put(p.getLeft(), p.getRight());
                }
            }
            params.put("stackTrace", ThrowableUtil.getStackTrace(t));
            getLogger().warn("{}", JackJsonUtil.toJSONString(params));
        }
    }

    /**
     * error
     *
     * @param t
     * @param pairs
     */
    default void error(Throwable t, Pair<?, ?>... pairs) {
        Map<Object, Object> params = new HashMap<>(pairs.length + 5);
        params.put("category", getCategory());
        if (!StringUtils.equals(getCategory(), ACCESS)) {
            params.put("level", "error");
        }
        params.put("threadName", getThreadName());
        params.put("requestId", RequestContext.getCurrentRequestId());
        for (Pair<?, ?> p : pairs) {
            if (p != null) {
                params.put(p.getLeft(), p.getRight());
            }
        }
        params.put("stackTrace", ThrowableUtil.getStackTrace(t));
        getLogger().error("{}", JackJsonUtil.toJSONString(params));
    }

    /**
     * error
     *
     * @param pairs
     */
    default void error(Pair<?, ?>... pairs) {
        Map<Object, Object> params = new HashMap<>(pairs.length + 4);
        if (!StringUtils.equals(getCategory(), ACCESS)) {
            params.put("level", "error");
        }
        params.put("requestId", RequestContext.getCurrentRequestId());
        params.put("category", getCategory());
        params.put("threadName", getThreadName());
        for (Pair<?, ?> p : pairs) {
            if (p != null) {
                params.put(p.getLeft(), p.getRight());
            }
        }
        getLogger().error("{}", JackJsonUtil.toJSONString(params));
    }
}
