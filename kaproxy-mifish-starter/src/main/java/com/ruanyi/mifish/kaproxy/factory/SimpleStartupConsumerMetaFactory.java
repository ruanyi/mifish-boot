package com.ruanyi.mifish.kaproxy.factory;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.RateLimiter;
import com.ruanyi.mifish.kaproxy.StartupConsumerMetaFactory;
import com.ruanyi.mifish.kaproxy.container.KaproxyConsumerMetaContainer;
import com.ruanyi.mifish.kaproxy.exceptions.ConsumerCreateException;
import com.ruanyi.mifish.kaproxy.model.*;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-11-17 21:02
 */
public class SimpleStartupConsumerMetaFactory implements StartupConsumerMetaFactory, EnvironmentAware {

    /** environment */
    private Environment environment;

    /**
     * @see StartupConsumerMetaFactory#batchBuild(Set, ConsumerStartupStrategy)
     */
    @Override
    public List<StartupConsumerMeta> batchBuild(Set<GroupTopic> groupTopics, ConsumerStartupStrategy strategy)
        throws Exception {
        if (strategy == null) {
            strategy = ConsumerStartupStrategy.ALL_IN_ONE;
        }
        List<StartupConsumerMeta> startupConsumerMetas = null;
        // 目前仅实现一种策略，后续策略，后续再实现
        if (strategy == ConsumerStartupStrategy.ALL_IN_ONE) {
            startupConsumerMetas = buildAllInOneStartupConsumerMetas(groupTopics);
        }
        return startupConsumerMetas;
    }

    /**
     * buildAllInOneStartupConsumerMetas
     * 
     * @param groupTopics
     * @return
     */
    private List<StartupConsumerMeta> buildAllInOneStartupConsumerMetas(Set<GroupTopic> groupTopics) {
        checkArgument(groupTopics != null && !groupTopics.isEmpty(),
            "groupTopics cannot be empty in StartupConsumerMetaFactory");
        // 默认值是-1
        String consumptionRateStr = this.environment.getProperty("mifish.processor.kaproxy.consumption-rate", "-1");
        int consumptionRate = Integer.parseInt(consumptionRateStr);
        Optional<RateLimiter> optionalRateLimiter = Optional.empty();
        if (consumptionRate > 0) {
            optionalRateLimiter = Optional.of(RateLimiter.create(consumptionRate));
        }
        //
        StartupConsumerMeta startupConsumerMeta = new StartupConsumerMeta(groupTopics, optionalRateLimiter);
        for (GroupTopic groupTopic : groupTopics) {
            KaproxyRequestMessage kaproxyRequestMessage = buildKaproxyRequestMessage(groupTopic);
            startupConsumerMeta.addKaproxyRequestMessage(kaproxyRequestMessage);
        }
        return Lists.newArrayList(startupConsumerMeta);
    }

    /**
     * buildKaproxyRequestMessage
     *
     * @param groupTopic
     * @return
     */
    private KaproxyRequestMessage buildKaproxyRequestMessage(GroupTopic groupTopic) {
        checkArgument(groupTopic != null, "groupTopic cannot be null in StartupConsumerMetaFactory");
        KaproxyConsumerMeta kaproxyConsumerMeta =
            KaproxyConsumerMetaContainer.getInstance().getConsumerMeta(groupTopic.getGroup(), groupTopic.getTopic());
        String poolName = kaproxyConsumerMeta.getPoolName();
        String host = this.environment.getProperty("mifish.processor.kaproxy." + poolName + ".host");
        String token = this.environment.getProperty("mifish.processor.kaproxy." + poolName + ".token");
        if (StringUtils.isBlank(host) || StringUtils.isBlank(token)) {
            throw new ConsumerCreateException("host or token is blank", groupTopic.getGroup(), groupTopic.getTopic());
        }
        String timeout = this.environment.getProperty("mifish.processor.kaproxy." + poolName + ".timeout", "500");
        String url = String.format("%s/group/%s/topic/%s?token=%s&timeout=%s", host, groupTopic.getGroup(),
            groupTopic.getTopic(), token, timeout);
        // 构建kaproxy发送消息的请求
        KaproxyRequestMessage kaproxyRequestMessage = new KaproxyRequestMessage();
        kaproxyRequestMessage.setUrl(url);
        kaproxyRequestMessage.setGroup(groupTopic.getGroup());
        kaproxyRequestMessage.setTopic(groupTopic.getTopic());
        kaproxyRequestMessage.setPoolName(poolName);
        kaproxyRequestMessage.setHost(host);
        kaproxyRequestMessage.setToken(token);
        return kaproxyRequestMessage;
    }

    /**
     * @see EnvironmentAware#setEnvironment(Environment)
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
