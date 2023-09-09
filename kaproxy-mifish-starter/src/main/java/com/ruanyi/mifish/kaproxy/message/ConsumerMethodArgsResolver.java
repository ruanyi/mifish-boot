package com.ruanyi.mifish.kaproxy.message;

import com.ruanyi.mifish.kaproxy.model.KaproxyConsumerMeta;
import com.ruanyi.mifish.kaproxy.model.QueueMessage;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2018-09-13 20:34
 */
public interface ConsumerMethodArgsResolver {

    /**
     * resolveArguments
     *
     * @param kaproxyConsumerMeta
     * @param message
     * @return
     * @throws Exception
     */
    Object[] resolveArguments(KaproxyConsumerMeta kaproxyConsumerMeta, QueueMessage message) throws Exception;
}
