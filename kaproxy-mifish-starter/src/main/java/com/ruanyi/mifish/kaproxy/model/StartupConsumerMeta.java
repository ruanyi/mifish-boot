package com.ruanyi.mifish.kaproxy.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.util.concurrent.RateLimiter;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2021-11-04 21:09
 */
public final class StartupConsumerMeta {

    /** kaproxyRequestMessages */
    private List<KaproxyRequestMessage> kaproxyRequestMessages = new ArrayList<>();

    /** groupTopics */
    private Set<GroupTopic> groupTopics;

    /**
     * 如果：StartupConsumerMeta.consumptionRate 为-1，则证明：不需要做限流，则该属性为：Optional.empty <br>
     * <p>
     * 否则，有实例
     */
    private Optional<RateLimiter> optionalRateLimiter;

    /**
     * StartupConsumerMeta
     *
     * @param groupTopics
     * @param optionalRateLimiter
     */
    public StartupConsumerMeta(Set<GroupTopic> groupTopics, Optional<RateLimiter> optionalRateLimiter) {
        this.groupTopics = groupTopics;
        this.optionalRateLimiter = optionalRateLimiter;
    }

    /**
     * getGroupTopics
     * 
     * @return
     */
    public Set<GroupTopic> getGroupTopics() {
        return groupTopics;
    }

    /**
     * getKaproxyRequestMessages
     * 
     * @return
     */
    public List<KaproxyRequestMessage> getKaproxyRequestMessages() {
        return kaproxyRequestMessages;
    }

    /**
     * getOptionalRateLimiter
     * 
     * @return
     */
    public Optional<RateLimiter> getOptionalRateLimiter() {
        return optionalRateLimiter;
    }

    /**
     * addKaproxyRequestMessage
     * 
     * @param kaproxyRequestMessage
     * @return
     */
    public StartupConsumerMeta addKaproxyRequestMessage(KaproxyRequestMessage kaproxyRequestMessage) {
        if (kaproxyRequestMessage != null) {
            this.kaproxyRequestMessages.add(kaproxyRequestMessage);
        }
        return this;
    }
}
