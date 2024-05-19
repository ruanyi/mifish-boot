package com.ruanyi.mifish.mqproxy.model;

/**
 * Description:
 * 
 * mqproxy启动时的meta配置信息
 *
 * @author: ruanyi
 * @Date: 2024-05-19 15:14
 */
public class MqproxyStartupMeta {

    /**
     * 让哪些group topic生效：真正开启消费， 该字段有一定的格式要求：
     *
     * 1、以分号(;)进行分割 <br/>
     * 2、group + topic如果一模一样，则可以不写 <br/>
     * 3、
     */
    private String effectGroupTopics;

    /**
     * 开启多少个消费线程个数，默认值：1 <br/>
     *
     * 类似：spring-kafka里的concurrency，本质目的是为了提高资源利用率 <br/>
     */
    private int consumeConcurrency = 1;

    /**
     * 消费速率
     *
     * 通过guava RateLimiter来实现，单机版
     *
     * 每秒消费多少个
     *
     * -1：代表没有限制
     *
     * Consumption rate
     */
    private int consumptionRate = -1;

    /**
     * 业务处理，并发度
     * 
     * 考虑到：一般情况下，业务处理比较慢，消费比较快，因此：handleCurrency >= concurrency
     * 
     * 例如：视频转码、视频meta信息获取等
     */
    private int handleConcurrency = 2;

    /** MqproxyStartupMeta */
    private MqproxyStartupMeta() {

    }

    /**
     * getEffectGroupTopics
     *
     * @return
     */
    public String getEffectGroupTopics() {
        return effectGroupTopics;
    }

    /**
     * getConcurrency
     *
     * @return
     */
    public int getConsumeConcurrency() {
        return consumeConcurrency;
    }

    /**
     * getConsumptionRate
     * 
     * @return
     */
    public int getConsumptionRate() {
        return consumptionRate;
    }

    /**
     * getHandleConcurrency
     * 
     * @return
     */
    public int getHandleConcurrency() {
        return handleConcurrency;
    }

    /**
     * of
     * 
     * @param effectGroupTopics
     * @return
     */
    public static MqproxyStartupMeta of(String effectGroupTopics) {
        return of(effectGroupTopics, 1, -1, 2);
    }

    /**
     * of
     * 
     * @param effectGroupTopics
     * @param consumeConcurrency
     * @param consumptionRate
     * @param handleConcurrency
     * @return
     */
    public static MqproxyStartupMeta of(String effectGroupTopics, int consumeConcurrency, int consumptionRate,
        int handleConcurrency) {
        MqproxyStartupMeta mqproxyStartupMeta = new MqproxyStartupMeta();
        mqproxyStartupMeta.effectGroupTopics = effectGroupTopics;
        mqproxyStartupMeta.consumeConcurrency = consumeConcurrency;
        mqproxyStartupMeta.consumptionRate = consumptionRate;
        mqproxyStartupMeta.handleConcurrency = handleConcurrency;
        return mqproxyStartupMeta;
    }
}
