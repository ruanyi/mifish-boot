package com.ruanyi.mifish.kaproxy.message;

import com.ruanyi.mifish.kaproxy.annotation.KaproxyConsumer;
import com.ruanyi.mifish.kaproxy.model.MessageStatus;
import com.ruanyi.mifish.kaproxy.model.QueueMessage;

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
     * @param kaproxyConsumer
     * @param message
     * @return
     */
    MessageStatus doChain(KaproxyConsumer kaproxyConsumer, QueueMessage message);

    /**
     * getMessageHandler
     *
     * @return
     */
    MessageHandler getMessageHandler();
}
