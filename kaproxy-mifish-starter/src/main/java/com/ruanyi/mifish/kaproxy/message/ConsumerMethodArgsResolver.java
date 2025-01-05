package com.ruanyi.mifish.kaproxy.message;

import com.ruanyi.mifish.kaproxy.model.AnnotationConsumerMeta;
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
     * @param annotationConsumerMeta
     * @param message
     * @return
     * @throws Exception
     */
    Object[] resolveArguments(AnnotationConsumerMeta annotationConsumerMeta, QueueMessage message) throws Exception;
}
