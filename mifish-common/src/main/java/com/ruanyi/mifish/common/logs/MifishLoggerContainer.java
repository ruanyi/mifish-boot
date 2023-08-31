package com.ruanyi.mifish.common.logs;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2019-03-27 20:11
 */
public final class MifishLoggerContainer {

    /**
     * 日志容器 key；category value：logger
     * <p>
     * 提供默认初始化日志配置的能力
     */
    private final Map<String, Logger> container = new ConcurrentHashMap<>();

    /**
     * addLogger
     *
     * @param category
     * @param logger
     */
    public void addLogger(String category, Logger logger) {
        if (StringUtils.isNotBlank(category) && logger != null) {
            container.put(category, logger);
        }
    }

    /**
     * addLogger
     *
     * @param logger
     */
    public void addLogger(Logger logger) {
        if (logger != null) {
            container.put(logger.getName(), logger);
        }
    }

    /**
     * addLogger
     *
     * @param category
     */
    public void addLogger(String category) {
        Logger logger = LoggerFactory.getLogger(category);
        addLogger(category, logger);
    }

    /**
     * getLogger
     *
     * @param category
     * @return
     */
    public Logger getLogger(String category) {
        return container.get(category);
    }

    /**
     * getAllLoggers
     *
     * @return
     */
    public Map<String, Logger> getAllLoggers() {
        return new HashMap<>(container);
    }

    /**
     * getInstance
     *
     * @return
     */
    public static MifishLoggerContainer getInstance() {
        return KeyValueLoggerContainerHolder.INSTANCE;
    }

    /**
     * Description:
     *
     * @author: rls
     * @Date: 2019-03-27 20:11
     */
    private static class KeyValueLoggerContainerHolder {

        /**
         * INSTANCE
         */
        private static final MifishLoggerContainer INSTANCE = new MifishLoggerContainer();
    }

    /**
     * 不能被实例化
     */
    private MifishLoggerContainer() {

    }
}
