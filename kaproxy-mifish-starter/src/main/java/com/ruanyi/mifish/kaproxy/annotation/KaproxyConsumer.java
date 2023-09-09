package com.ruanyi.mifish.kaproxy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ruanyi.mifish.common.model.MessageType;
import com.ruanyi.mifish.kaproxy.model.QueueMessage;

/**
 * Description:
 *
 * 基于kaproxy实现的消费机制
 *
 * @author: ruanyi
 * @Date: 2022-11-17 13:49
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface KaproxyConsumer {

    /**
     * 必须配置，支持多topic消费
     *
     * PS：虽说，支持多topic，但是，消息格式最好是一摸一样的
     *
     * @return
     */
    String[] topics() default {};

    /**
     * The topic pattern for this XiuxiuKafkaConsumer. The entries can be 'property-placeholder keys'
     * <p>
     * 配置方式跟：@Value的形式，一模一样
     * <p>
     * topicPattern = "${xxxx.xxxx.xxx.xxx}" 优先从环境变量中获取，然后，在从properties中获取
     * <p>
     * 实现一部分的：KafkaListener中topicPattern功能
     *
     * 虽说支持多topic，但是，消息格式最好是一摸一样的，若不一样，则只能使用统一的类：QueueMessage
     *
     * @see QueueMessage
     *
     * @return
     */
    String topicPattern() default "";

    /**
     * 仅支持单group
     * <p>
     * 目前主流社区的解决方案：也是：单group，多topic方案
     *
     * @return
     */
    String group();

    /**
     * 最大重试消费次数
     * <p>
     * 需在应用层代码返回：MessageStatus.RETRY
     * <p>
     * 或者框架层收集到某些特定的error_code
     *
     * @return
     */
    int maxRetryCount() default 3;

    /**
     * 重试的时间间隔，单位：毫秒 默认是500ms
     * <p>
     * 配置小于0 ，则默认不休眠
     *
     * @return
     */
    long retryInterval() default 500;

    /**
     * 不同系统之间传输和共享的特殊结构化数据的标准或规范。
     * 
     * 默认是：json
     *
     * @return
     */
    MessageType messageType() default MessageType.JSON;

    /**
     * kaproxy资源池的名称
     *
     * @return
     */
    String poolName() default "default";
}
