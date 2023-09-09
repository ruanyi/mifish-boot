package com.ruanyi.mifish.kaproxy.message.engine;

import static com.ruanyi.mifish.kaproxy.model.QueueMessage.LOCAL_QUEUE_ELAPSE;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.ruanyi.mifish.kaproxy.annotation.KaproxyConsumer;
import com.ruanyi.mifish.kaproxy.message.MessageChain;
import com.ruanyi.mifish.kaproxy.message.MessageDigestEngine;
import com.ruanyi.mifish.kaproxy.message.MessageHandler;
import com.ruanyi.mifish.kaproxy.message.MessageNode;
import com.ruanyi.mifish.kaproxy.message.chain.SimpleMessageChain;
import com.ruanyi.mifish.kaproxy.model.MessageStatus;
import com.ruanyi.mifish.kaproxy.model.QueueMessage;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2019-04-11 20:20
 */
public class SimpleMessageDigestEngine implements MessageDigestEngine, ApplicationContextAware {

    /** messageHandler */
    private MessageHandler messageHandler;

    /** applicationContext */
    private ApplicationContext applicationContext;

    /**
     * SimpleMessageDigestEngine
     *
     * @param messageHandler
     */
    public SimpleMessageDigestEngine(MessageHandler messageHandler) {
        Objects.requireNonNull(messageHandler, "MessageHandler cannot be null in MessageDigestEngine");
        this.messageHandler = messageHandler;
    }

    /**
     * @see MessageDigestEngine#digest(KaproxyConsumer, QueueMessage)
     */
    @Override
    public MessageStatus digest(KaproxyConsumer kaproxyConsumer, QueueMessage message) {
        // 计算本地队列等待的时间
        long localQueueElapse = System.currentTimeMillis() - message.getDequeueTimeMillis();
        message.addAttribute(LOCAL_QUEUE_ELAPSE, localQueueElapse);
        List<MessageNode> messageNodes = obtainAllMessageNodes();
        MessageChain messageChain = SimpleMessageChain.buildSimpleChain(this.messageHandler, messageNodes);
        // 由消息齿轮带动消息链条继续往下处理消息
        MessageStatus msgStatus = messageChain.doChain(kaproxyConsumer, message);
        // return msg status
        return msgStatus;
    }

    /**
     * obtainAllMessageNodes
     *
     * @return
     */
    private List<MessageNode> obtainAllMessageNodes() {
        List<MessageNode> ret = new ArrayList<>();
        String[] names = applicationContext.getBeanNamesForType(MessageNode.class);
        if (names == null || names.length == 0) {
            return ret;
        }
        for (String name : names) {
            ret.add(applicationContext.getBean(name, MessageNode.class));
        }
        return ret;
    }

    /**
     * @see MessageDigestEngine#getMessageHandler()
     */
    @Override
    public MessageHandler getMessageHandler() {
        return this.messageHandler;
    }

    /**
     * @see ApplicationContextAware#setApplicationContext(ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
