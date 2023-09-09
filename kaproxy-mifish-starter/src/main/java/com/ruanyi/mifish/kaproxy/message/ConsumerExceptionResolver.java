package com.ruanyi.mifish.kaproxy.message;


import com.ruanyi.mifish.kaproxy.model.MessageStatus;
import com.ruanyi.mifish.kaproxy.model.QueueMessage;

/**
 * Interface to be implemented by objects that can resolve exceptions thrown during
 * handler mapping or execution, in the typical case to error views. Implementors are
 * typically registered as beans in the application context.
 *
 * <p>Error views are analogous to JSP error pages but can be used with any kind of
 * exceptions including any checked exceptions, with potentially fine-grained mappings for
 * specific handlers.
 *
 * @author: ruanyi
 * @Date: 2018-09-29 15:16
 */
public interface ConsumerExceptionResolver {

    /**
     * 处理队列处理机的异常
     *
     * @param th
     * @param message
     * @return
     */
    MessageStatus resolveException(Throwable th, QueueMessage message);
}
