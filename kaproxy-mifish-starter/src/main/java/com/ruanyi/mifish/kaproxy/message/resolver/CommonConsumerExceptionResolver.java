package com.ruanyi.mifish.kaproxy.message.resolver;

import java.lang.reflect.InvocationTargetException;

import com.ruanyi.mifish.common.ex.MissingRequestParameterException;
import com.ruanyi.mifish.kaproxy.message.ConsumerExceptionResolver;
import com.ruanyi.mifish.kaproxy.model.MessageStatus;
import com.ruanyi.mifish.kaproxy.model.QueueMessage;

/**
 * Description:
 * <p>
 * 处理机的统一异常处理机制
 *
 * @author: ruanyi
 * @Date: 2018-09-29 15:24
 */
public class CommonConsumerExceptionResolver extends AbstractConsumerExceptionResolver {

    /**
     * @see ConsumerExceptionResolver#resolveException(Throwable, QueueMessage)
     */
    @Override
    public MessageStatus resolveException(Throwable th, QueueMessage message) {
        try {
            if (th instanceof InvocationTargetException) {
                throw ((InvocationTargetException)th).getTargetException();
            }
            throw th;
        } catch (MissingRequestParameterException ex) {
            return handleMissingRequestParameterException(ex, message);
        } catch (IllegalArgumentException ex) {
            return handleIllegalArgumentException(ex, message);
        } catch (ClassCastException ex) {
            return handleClassCastException(ex, message);
        } catch (Throwable newth) {
            return handleException(newth, message);
        }
    }
}
