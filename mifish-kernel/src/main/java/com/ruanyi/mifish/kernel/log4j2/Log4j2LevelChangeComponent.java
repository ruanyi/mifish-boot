package com.ruanyi.mifish.kernel.log4j2;

import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.collect.Maps;
import com.ruanyi.mifish.common.logs.MifishLoggerContainer;
import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.common.utils.UUIDUtil;

/**
 * Description: com.meitu.xiuxiu.framework.log4j2
 *
 * @author: rls
 * @Date: 2019-01-15
 */
public class Log4j2LevelChangeComponent implements InitializingBean {

    private static final String KEY_LOGGER_CONFIG_LEVEL = "log4j2.configuration.level";

    /**
     * LOGGER
     */
    private static final MifishLogs LOGGER = MifishLogs.framework;

    /**
     * loggerLevels
     */
    private Map<String, Level> loggerLevels = Maps.newHashMap();

    /**
     * afterPropertiesSet
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        initLoggerConfig();
        initLoggerLevels();
        listenLogConfigChange();
    }

    /**
     * 假如没有初始化，则使用默认的配置
     */
    private void initLoggerConfig() {
        LoggerContext ctx = (LoggerContext)LogManager.getContext(false);
        org.apache.logging.log4j.core.config.Configuration config = ctx.getConfiguration();
        Map<String, LoggerConfig> loggerConfigMap = config.getLoggers();
        Map<String, Logger> allLoggers = MifishLoggerContainer.getInstance().getAllLoggers();
        for (Map.Entry<String, Logger> entry : allLoggers.entrySet()) {
            String category = entry.getKey();
            Logger logger = entry.getValue();
            // 假如log4j2没有配置，则使用默认配置
            if (!loggerConfigMap.containsKey(category)) {
                // 本地没有必要初始化大数据日志
                if (isInitActivityLogger(logger)) {
                    initActivityLoggerConfig(ctx, logger);
                } else {
                    initDefaultLoggerConfig(ctx, category);
                }
            }
        }
    }

    /**
     * 判断是否需要初始化数据日志
     *
     * @param logger
     * @return
     */
    static boolean isInitActivityLogger(Logger logger) {
        return logger != null && StringUtils.equals(logger.getName(), "activity");
    }

    /**
     * initActivityLoggerConfig
     * <p>
     * 初始化大数据日志的配置，固定写死，约定大于配置
     *
     * @param ctx
     * @param logger
     */
    private void initActivityLoggerConfig(LoggerContext ctx, Logger logger) {
        try {
            Configuration config = ctx.getConfiguration();
            final Layout layout = PatternLayout.newBuilder().withPattern(PatternLayout.DEFAULT_CONVERSION_PATTERN)
                .withConfiguration(config).withCharset(Charset.forName("utf-8")).build();
            TimeBasedTriggeringPolicy timeBasedTriggeringPolicy =
                TimeBasedTriggeringPolicy.newBuilder().withInterval(1).withModulate(true).build();
            RollingFileAppender appender = RollingFileAppender.newBuilder().withName(UUIDUtil.obtainUUID())
                .withFileName("/www/arachnia_log/processor-server-meitu/bigdata.log")
                .withFilePattern("/www/arachnia_log/processor-server-meitu/bigdata.log2-%d{yyyyMMdd-HH}")
                .setConfiguration(config).withLayout(layout).withPolicy(timeBasedTriggeringPolicy).build();
            appender.start();
            config.addAppender(appender);
            AppenderRef ref = AppenderRef.createAppenderRef(appender.getName(), null, null);
            AppenderRef[] refs = new AppenderRef[] {ref};
            LoggerConfig loggerConfig =
                LoggerConfig.createLogger(false, Level.INFO, logger.getName(), null, refs, null, config, null);
            loggerConfig.addAppender(appender, null, null);
            config.addLogger(logger.getName(), loggerConfig);
            ctx.updateLoggers();
        } catch (Exception ex) {
            LOGGER.warn(ex, Pair.of("method", "initActivityLoggerConfig"), Pair.of("status", "exceptions"),
                Pair.of("clazz", "Log4j2StartupInitRunner"),
                Pair.of("loggerName", logger != null ? logger.getName() : ""));
        }
    }

    /**
     * initDefaultLoggerConfig
     *
     * @param ctx
     * @param loggerName
     */
    private void initDefaultLoggerConfig(LoggerContext ctx, String loggerName) {
        Configuration config = ctx.getConfiguration();
        final Layout layout = PatternLayout.newBuilder().withPattern(PatternLayout.DEFAULT_CONVERSION_PATTERN)
            .withConfiguration(config).withCharset(Charset.forName("utf-8")).build();
        ConsoleAppender appender = ConsoleAppender.createDefaultAppenderForLayout(layout);
        config.addAppender(appender);
        appender.start();
        AppenderRef ref = AppenderRef.createAppenderRef(appender.getName(), null, null);
        AppenderRef[] refs = new AppenderRef[] {ref};
        LoggerConfig loggerConfig =
            LoggerConfig.createLogger(false, Level.INFO, loggerName, null, refs, null, config, null);
        loggerConfig.addAppender(appender, null, null);
        config.addLogger(loggerName, loggerConfig);
        ctx.updateLoggers();
    }

    /**
     * 监听日志的配置变化，如果删除配置，则所有日志，回归最初的模样
     */
    private void listenLogConfigChange() {
        // ironClient = IronClientManager.getIronClient();
        // if (ironClient != null) {
        // try {
        // ironClient.watchEvents(ironEvent -> {
        // for (ConfigEvent ce : ironEvent.getAllEvents()) {
        // ConfigEntry configEntry = ce.getConfigEntry();
        // // 只处理跟logger配置变更相关的事件
        // if (StringUtils.equals(KEY_LOGGER_CONFIG_LEVEL, configEntry.getKey())) {
        // if (ce instanceof SetEvent) {
        // String val = configEntry.getValue() == null ? "" : configEntry.getValue();
        // Map<String, String> cmds = JSON.parseObject(val, Map.class);
        // this.batchLogLevelSet(cmds);
        // } else if (ce instanceof DeleteEvent) {
        // logLevelSet("all", "reset");
        // }
        // }
        // }
        // });
        // } catch (Exception e) {
        // LOGGER.error(Pair.of("clazz", "Log4j2LevelChangeComponent"), Pair.of("method", "listenLogConfigChange"),
        // Pair.of("loggerLevels", this.loggerLevels), Pair.of("message", e.getMessage()));
        // }
        // }
    }

    /**
     * 缓存：获取当前系统的所有logger的级别 以便后续，重置会最初的模样
     */
    private void initLoggerLevels() {
        LoggerContext ctx = (LoggerContext)LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        Map<String, LoggerConfig> loggerConfigs = config.getLoggers();
        if (loggerConfigs == null || loggerConfigs.isEmpty()) {
            return;
        }
        for (Map.Entry<String, LoggerConfig> entry : loggerConfigs.entrySet()) {
            loggerLevels.put(entry.getKey(), entry.getValue().getLevel());
        }
        // default return true
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(Pair.of("clazz", "Log4j2LevelChangeComponent"), Pair.of("method", "initLoggerLevels"),
                Pair.of("loggerLevels", this.loggerLevels));
        }
    }

    /**
     * batchLogLevelSet
     *
     * @param cmds
     * @return
     */
    public boolean batchLogLevelSet(Map<String, String> cmds) {
        if (cmds == null || cmds.isEmpty()) {
            return false;
        }
        if (cmds.containsKey("all")) {
            return logLevelSet("all", cmds.get("all"));
        } else {
            for (Map.Entry<String, String> entry : cmds.entrySet()) {
                logLevelSet(entry.getKey(), entry.getValue());
            }
            return true;
        }
    }

    /**
     * logLevelSet
     *
     * @param log
     * @param cmd
     * @return
     */
    public boolean logLevelSet(String log, String cmd) {
        boolean isSuccess = true;
        if (!StringUtils.equalsIgnoreCase("all", log)) {
            isSuccess = StringUtils.equalsIgnoreCase("reset", cmd) ? logSetLevel(log, this.loggerLevels.get(log))
                : logSetLevel(log, Level.toLevel(cmd));
        } else {
            // 设置all的日志级别
            for (Map.Entry<String, Level> entry : this.loggerLevels.entrySet()) {
                if (StringUtils.equalsIgnoreCase("reset", cmd)) {
                    isSuccess = logSetLevel(entry.getKey(), entry.getValue());
                } else {
                    isSuccess = logSetLevel(entry.getKey(), Level.toLevel(cmd));
                }
            }
        }
        // default return true
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(Pair.of("clazz", "Log4j2LevelChangeComponent"), Pair.of("method", "logLevelSet"),
                Pair.of("log", log), Pair.of("cmd", cmd), Pair.of("isSuccess", isSuccess),
                Pair.of("loggerLevels", this.loggerLevels));
        }
        return isSuccess;
    }

    /**
     * logSetLevel
     *
     * @param log
     * @param level
     * @return
     */
    private boolean logSetLevel(String log, Level level) {
        if (level == null || !this.loggerLevels.containsKey(log)) {
            return false;
        }
        LoggerContext ctx = (LoggerContext)LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(log);
        if (loggerConfig != null) {
            loggerConfig.setLevel(level);
            ctx.updateLoggers();
        }
        return true;
    }
}
