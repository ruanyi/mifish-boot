package com.ruanyi.mifish.kaproxy.factory;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import com.ruanyi.mifish.kaproxy.KaproxyRequestMessageFactory;
import com.ruanyi.mifish.kaproxy.container.KaproxyConsumerMetaContainer;
import com.ruanyi.mifish.kaproxy.container.KaproxyRequestMessageContainer;
import com.ruanyi.mifish.kaproxy.exceptions.ConsumerCreateException;
import com.ruanyi.mifish.kaproxy.model.AnnotationConsumerMeta;
import com.ruanyi.mifish.kaproxy.model.GroupTopic;
import com.ruanyi.mifish.kaproxy.model.KaproxyRequestMessage;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-19 14:56
 */
public class SimpleKaproxyRequestMessageFactory implements KaproxyRequestMessageFactory, EnvironmentAware {

    /** environment */
    private Environment environment;

    /** mqproxyConsumerMetaContainer */
    private KaproxyConsumerMetaContainer kaproxyConsumerMetaContainer = KaproxyConsumerMetaContainer.getInstance();

    /**
     * @see KaproxyRequestMessageFactory#batchBuild(String)
     */
    @Override
    public KaproxyRequestMessageContainer batchBuild(String effectGroupTopics) {
        // 1、 init effect group topics
        KaproxyRequestMessageContainer kaproxyRequestMessageContainer = KaproxyRequestMessageContainer.getInstance();
        Set<GroupTopic> gtSet = GroupTopic.parse2Set(effectGroupTopics);
        // 2、计算总的g-t权重
        int sum = 0;
        for (GroupTopic groupTopic : gtSet) {
            sum += groupTopic.getPriority();
        }
        //
        for (GroupTopic groupTopic : gtSet) {
            // 为了实现优先级调度，优先级越高，权重就越大 Size就越大
            int weight = (int)Math.ceil(groupTopic.getPriority() * 100.0d / sum);
            KaproxyRequestMessage kaproxyRequestMessage = buildMqproxyRequestMessage(groupTopic);
            kaproxyRequestMessageContainer.addMqproxyRequestMessages(kaproxyRequestMessage, weight);
        }
        return kaproxyRequestMessageContainer;
    }

    /**
     * buildMqproxyRequestMessage
     *
     * @param groupTopic
     * @return
     */
    private KaproxyRequestMessage buildMqproxyRequestMessage(GroupTopic groupTopic) {
        checkArgument(groupTopic != null, "groupTopic cannot be null in MqproxyRequestMessageFactory");
        AnnotationConsumerMeta annotationConsumerMeta =
            this.kaproxyConsumerMetaContainer.getConsumerMeta(groupTopic.getGroup(), groupTopic.getTopic());
        String poolName = annotationConsumerMeta.getPoolName();
        String host = this.environment.getProperty("mifish.processor.mqproxy." + poolName + ".host");
        String token = this.environment.getProperty("mifish.processor.mqproxy." + poolName + ".token");
        if (StringUtils.isBlank(host) || StringUtils.isBlank(token)) {
            throw new ConsumerCreateException("host or token is blank", groupTopic.getGroup(), groupTopic.getTopic());
        }
        String timeout = this.environment.getProperty("mifish.processor.mqproxy." + poolName + ".timeout", "500");
        String url = String.format("%s/group/%s/topic/%s?token=%s&timeout=%s", host, groupTopic.getGroup(),
            groupTopic.getTopic(), token, timeout);
        // 构建mqproxy发送消息的请求
        KaproxyRequestMessage kaproxyRequestMessage = new KaproxyRequestMessage();
        kaproxyRequestMessage.setGroupTopic(groupTopic);
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
