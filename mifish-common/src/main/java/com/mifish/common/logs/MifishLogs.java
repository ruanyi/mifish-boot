package com.mifish.common.logs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2018-08-22 17:29
 */
public enum MifishLogs implements KeyValueLogger {

    /**
     * 框架层需要使用这个日志
     */
    framework("framework"),

    /**
     * 处理机的业务日志
     */
    processor("processor"),

    /**
     * 访问日志，一般代码不会用。用在tomcat和nginx中的访问日志。
     */
    access("access"),
    ;

    /**
     * category
     */
    private String category;

    /**
     * log4j2
     */
    private Logger logger;

    /**
     * FrameLogs
     *
     * @param category
     */
    MifishLogs(String category) {
        this.category = category;
        this.logger = LoggerFactory.getLogger(category);
        KeyValueLogger.register(this.logger);
    }

    /**
     * getCategory
     *
     * @return
     */
    @Override
    public String getCategory() {
        return category;
    }

    /**
     * getLogger
     *
     * @return
     */
    @Override
    public Logger getLogger() {
        return logger;
    }
}
