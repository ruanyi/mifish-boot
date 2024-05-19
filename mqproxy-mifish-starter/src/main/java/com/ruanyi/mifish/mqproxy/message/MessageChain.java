package com.ruanyi.mifish.mqproxy.message;

import com.ruanyi.mifish.mqproxy.annotation.MqproxyConsumer;
import com.ruanyi.mifish.mqproxy.model.MessageStatus;
import com.ruanyi.mifish.mqproxy.model.QueueMessage;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2019-04-11 20:33
 */
public interface MessageChain {

    /**
     * doChain
     *
     * @param mqproxyConsumer
     * @param message
     * @return
     */
    MessageStatus doChain(MqproxyConsumer mqproxyConsumer, QueueMessage message);

    /**
     * getMessageHandler
     *
     * @return
     */
    MessageHandler getMessageHandler();
}
