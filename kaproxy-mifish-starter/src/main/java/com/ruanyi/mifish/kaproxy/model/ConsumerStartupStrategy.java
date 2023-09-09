package com.ruanyi.mifish.kaproxy.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2021-11-05 17:50
 */
public enum ConsumerStartupStrategy {

    /**
     * 只启动一个消费者任务，去消费kaproxy的任务
     *
     * 使用在云处理这种，重CPU的消费者任务
     *
     * 默认策略
     */
    ALL_IN_ONE("all_in_one", "只启动一个消费任务"),

    /**
     * 介于两者之间
     */
    GOUP_BY_POOLNAME("group_by_poolname", "根据资源池的名字，启动多个消费任务"),

    /**
     * 一个group + ":" + topic，启动一个消费任务， 高性能高并发时使用
     */
    GROUP_BY_GROUP_TOPIC("group_by_group_topic", "group+topic的最小纬度启动");

    /** code */
    private String code;

    /** desc */
    private String desc;

    /**
     * ConsumerStartupStrategy
     *
     * @param code
     * @param desc
     */
    private ConsumerStartupStrategy(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * getCode
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * getDesc
     *
     * @return
     */
    public String getDesc() {
        return desc;
    }

    /**
     * of
     * 
     * @param strategy
     * @return
     */
    public static ConsumerStartupStrategy of(String strategy) {
        for (ConsumerStartupStrategy css : values()) {
            if (StringUtils.equals(css.code, strategy)) {
                return css;
            }
        }
        return null;
    }
}
