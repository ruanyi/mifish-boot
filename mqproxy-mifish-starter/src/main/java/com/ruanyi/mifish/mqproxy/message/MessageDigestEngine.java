package com.ruanyi.mifish.mqproxy.message;

import com.ruanyi.mifish.mqproxy.annotation.MqproxyConsumer;
import com.ruanyi.mifish.mqproxy.model.MessageStatus;
import com.ruanyi.mifish.mqproxy.model.QueueMessage;

/**
 * Description:
 *
 * 消息消化引擎
 *
 * @author: ruanyi
 * @Date: 2019-04-11 20:16
 */
public interface MessageDigestEngine {

    /**
     * digest
     *
     * @param mqproxyConsumer
     * @param message
     * @return
     */
    MessageStatus digest(MqproxyConsumer mqproxyConsumer, QueueMessage message);

    /**
     * getMessageHandler
     *
     * @return
     */
    MessageHandler getMessageHandler();
}
