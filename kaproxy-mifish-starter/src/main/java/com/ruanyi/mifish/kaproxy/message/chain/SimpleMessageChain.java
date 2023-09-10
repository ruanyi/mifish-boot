package com.ruanyi.mifish.kaproxy.message.chain;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.ruanyi.mifish.common.utils.ThreadUtils;
import com.ruanyi.mifish.kaproxy.annotation.KaproxyConsumer;
import com.ruanyi.mifish.kaproxy.message.MessageChain;
import com.ruanyi.mifish.kaproxy.message.MessageHandler;
import com.ruanyi.mifish.kaproxy.message.MessageNode;
import com.ruanyi.mifish.kaproxy.model.MessageStatus;
import com.ruanyi.mifish.kaproxy.model.QueueMessage;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2019-04-11 20:45
 */
public class SimpleMessageChain implements MessageChain {

    /** messageHandler */
    private MessageHandler messageHandler;

    /**
     * iterator
     */
    private Iterator<MessageNode> iterator;

    /**
     * SimpleMessageChain
     *
     * @param messageHandler
     * @param messageNodes
     */
    private SimpleMessageChain(MessageHandler messageHandler, List<MessageNode> messageNodes) {
        Objects.requireNonNull(messageHandler, "MessageHandler cannot be null in MessageChain");
        Objects.requireNonNull(messageNodes, "MessageNodes cannot be null in MessageChain");
        this.messageHandler = messageHandler;
        this.iterator = messageNodes.iterator();
    }

    /**
     * @see MessageChain#doChain(KaproxyConsumer, QueueMessage)
     */
    @Override
    public MessageStatus doChain(KaproxyConsumer kaproxyConsumer, QueueMessage message) {
        if (this.iterator.hasNext()) {
            MessageNode messageNode = this.iterator.next();
            return messageNode.doNode(kaproxyConsumer, message, this);
        } else {
            // 增加重试能力
            while (true) {
                MessageStatus msgStatus = this.messageHandler.handle(message);
                if (msgStatus == MessageStatus.RETRY && message.incInvokedCount() <= kaproxyConsumer.maxRetryCount()) {
                    // 休眠一段时间继续
                    ThreadUtils.sleep(kaproxyConsumer.retryInterval());
                    continue;
                } else {
                    // 否则，返回消息状态
                    return msgStatus;
                }
            }
        }
    }

    /**
     * getMessageHandler
     *
     * @return
     */
    @Override
    public MessageHandler getMessageHandler() {
        return this.messageHandler;
    }

    /**
     * buildSimpleChain
     *
     * @param messageHandler
     * @param messageNodes
     * @return
     */
    public static MessageChain buildSimpleChain(MessageHandler messageHandler, List<MessageNode> messageNodes) {
        checkArgument(messageNodes != null, "checkNodes cannot be null in SimpleXiuxiuCheckChain");
        Collections.sort(messageNodes);
        return new SimpleMessageChain(messageHandler, messageNodes);
    }
}
