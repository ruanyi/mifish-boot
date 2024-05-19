package com.ruanyi.mifish.mqproxy.message;

import com.ruanyi.mifish.mqproxy.model.MessageStatus;
import com.ruanyi.mifish.mqproxy.model.QueueMessage;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2017-12-05 18:07
 */
public interface MessageHandler {

    /**
     * handle
     * <p>
     * 不允许抛出异常，根据MessageStatus来进行判断
     *
     * @param message
     * @return
     */
    MessageStatus handle(QueueMessage message);

    /**
     * setConsumerMethodArgsResolver
     *
     * @param consumerMethodArgsResolver
     */
    void setConsumerMethodArgsResolver(ConsumerMethodArgsResolver consumerMethodArgsResolver);

    /**
     * setConsumerExceptionResolver
     *
     * @param consumerExceptionResolver
     */
    void setConsumerExceptionResolver(ConsumerExceptionResolver consumerExceptionResolver);
}
