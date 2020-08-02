package com.ruanyi.mifish.kernel.log4j2;

import org.apache.logging.log4j.LogManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 *
 * mifish-boot默认引擎用Log4j2，暂不提供切换成其他日志的方式
 *
 * 业务方麻烦请使用slf4j门面框架，参考阿里巴巴的代码规范
 *
 * @author: rls
 * @Date: 2019-01-15
 */
@Configuration
@ConditionalOnClass(LogManager.class)
public class Log4j2LevelChangeConfiguration {

    @Bean
    public Log4j2LevelChangeComponent newLoggerChangeComponent() {
        return new Log4j2LevelChangeComponent();
    }
}
