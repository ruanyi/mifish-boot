package com.ruanyi.mifish.kaproxy.message;

import com.ruanyi.mifish.kaproxy.annotation.KaproxyConsumer;
import com.ruanyi.mifish.kaproxy.model.MessageStatus;
import com.ruanyi.mifish.kaproxy.model.QueueMessage;

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
     * @param kaproxyConsumer
     * @param message
     * @return
     */
    MessageStatus digest(KaproxyConsumer kaproxyConsumer, QueueMessage message);

    /**
     * getMessageHandler
     *
     * @return
     */
    MessageHandler getMessageHandler();
}
