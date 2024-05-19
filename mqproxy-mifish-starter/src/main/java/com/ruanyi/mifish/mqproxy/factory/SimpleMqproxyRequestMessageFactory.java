package com.ruanyi.mifish.mqproxy.factory;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import com.ruanyi.mifish.mqproxy.MqproxyRequestMessageFactory;
import com.ruanyi.mifish.mqproxy.container.MqproxyConsumerMetaContainer;
import com.ruanyi.mifish.mqproxy.container.MqproxyRequestMessageContainer;
import com.ruanyi.mifish.mqproxy.exceptions.ConsumerCreateException;
import com.ruanyi.mifish.mqproxy.model.AnnotationConsumerMeta;
import com.ruanyi.mifish.mqproxy.model.GroupTopic;
import com.ruanyi.mifish.mqproxy.model.MqproxyRequestMessage;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-19 14:56
 */
public class SimpleMqproxyRequestMessageFactory implements MqproxyRequestMessageFactory, EnvironmentAware {

    /** environment */
    private Environment environment;

    /** mqproxyConsumerMetaContainer */
    private MqproxyConsumerMetaContainer mqproxyConsumerMetaContainer = MqproxyConsumerMetaContainer.getInstance();

    /**
     * @see MqproxyRequestMessageFactory#batchBuild(String)
     */
    @Override
    public MqproxyRequestMessageContainer batchBuild(String effectGroupTopics) {
        // 1、 init effect group topics
        MqproxyRequestMessageContainer mqproxyRequestMessageContainer = MqproxyRequestMessageContainer.getInstance();
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
            MqproxyRequestMessage mqproxyRequestMessage = buildMqproxyRequestMessage(groupTopic);
            mqproxyRequestMessageContainer.addMqproxyRequestMessages(mqproxyRequestMessage, weight);
        }
        return mqproxyRequestMessageContainer;
    }

    /**
     * buildMqproxyRequestMessage
     *
     * @param groupTopic
     * @return
     */
    private MqproxyRequestMessage buildMqproxyRequestMessage(GroupTopic groupTopic) {
        checkArgument(groupTopic != null, "groupTopic cannot be null in MqproxyRequestMessageFactory");
        AnnotationConsumerMeta annotationConsumerMeta =
            this.mqproxyConsumerMetaContainer.getConsumerMeta(groupTopic.getGroup(), groupTopic.getTopic());
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
        MqproxyRequestMessage mqproxyRequestMessage = new MqproxyRequestMessage();
        mqproxyRequestMessage.setGroupTopic(groupTopic);
        mqproxyRequestMessage.setUrl(url);
        mqproxyRequestMessage.setGroup(groupTopic.getGroup());
        mqproxyRequestMessage.setTopic(groupTopic.getTopic());
        mqproxyRequestMessage.setPoolName(poolName);
        mqproxyRequestMessage.setHost(host);
        mqproxyRequestMessage.setToken(token);
        return mqproxyRequestMessage;
    }

    /**
     * @see EnvironmentAware#setEnvironment(Environment)
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
