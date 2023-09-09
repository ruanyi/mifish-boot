package com.ruanyi.mifish.kaproxy.ex;

import static com.ruanyi.mifish.common.utils.Constants.RESP_ERROR_CODE;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.tuple.Pair;

import com.ruanyi.mifish.common.ex.BusinessException;
import com.ruanyi.mifish.common.ex.ErrorCode;
import com.ruanyi.mifish.common.ex.MissingRequestParameterException;
import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.kaproxy.message.ConsumerExceptionResolver;
import com.ruanyi.mifish.kaproxy.model.MessageStatus;
import com.ruanyi.mifish.kaproxy.model.QueueMessage;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2019-03-29 13:36
 */
public class KaproxyConsumerExceptionResolver implements ConsumerExceptionResolver {

    /** log */
    private static final MifishLogs LOG = MifishLogs.framework;

    /**
     * resolveException
     *
     * @param th
     * @param message
     * @return
     */
    @Override
    public MessageStatus resolveException(Throwable th, QueueMessage message) {
        try {
            if (th instanceof InvocationTargetException) {
                throw ((InvocationTargetException)th).getTargetException();
            }
            throw th;
        } catch (BusinessException bex) {
            return handleBusinessException(bex, message);
        } catch (IllegalArgumentException | MissingRequestParameterException | ClassCastException ex) {
            return handleBindException(ex, message);
        } catch (Throwable newth) {
            return handleException(newth, message);
        }
    }

    /**
     * 统一处理其他异常
     *
     * @param th 异常
     * @param message
     * @return
     */
    public MessageStatus handleException(Throwable th, QueueMessage message) {
        String key = message.getAttribute(QueueMessage.MSG_KEY, String.class);
        String clientId = message.getAttribute(QueueMessage.CLIENT_ID, String.class);
        Long offset = message.getAttribute(QueueMessage.OFFSET, Long.class);
        // add error code
        message.addAttribute(RESP_ERROR_CODE, ErrorCode.UNKNOW_EXCEPTION.getCode());
        LOG.error(th, Pair.of("clazz", "BajieConsumerExceptionResolver"), Pair.of("method", "handleException"),
            Pair.of("group", message.getGroup()), Pair.of("partition", message.getPartition()),
            Pair.of("topic", message.getTopic()), Pair.of("message", message.getMessage()), Pair.of("key", key),
            Pair.of("clientId", clientId), Pair.of("offset", offset));
        return MessageStatus.SYS_EX;
    }

    /**
     * handleBindException
     *
     * @param ex
     * @param message
     * @return
     */
    public MessageStatus handleBindException(Exception ex, QueueMessage message) {
        String key = message.getAttribute(QueueMessage.MSG_KEY, String.class);
        String clientId = message.getAttribute(QueueMessage.CLIENT_ID, String.class);
        Long offset = message.getAttribute(QueueMessage.OFFSET, Long.class);
        LOG.warn(ex, Pair.of("clazz", "BajieConsumerExceptionResolver"), Pair.of("method", "handleBindException"),
            Pair.of("group", message.getGroup()), Pair.of("partition", message.getPartition()),
            Pair.of("topic", message.getTopic()), Pair.of("message", message.getMessage()), Pair.of("key", key),
            Pair.of("clientId", clientId), Pair.of("offset", offset));
        if (ex instanceof MissingRequestParameterException) {
            // add error code
            message.addAttribute(RESP_ERROR_CODE, ErrorCode.CLIENT_PARAM_MISS.getCode());
            return MessageStatus.FAILURE;
        } else if (ex instanceof IllegalArgumentException) {
            // add error code
            message.addAttribute(RESP_ERROR_CODE, ErrorCode.ILLEGAL_ARGUMENT.getCode());
            return MessageStatus.FAILURE;
        } else if (ex instanceof ClassCastException) {
            // add error code
            message.addAttribute(RESP_ERROR_CODE, ErrorCode.ILLEGAL_ARGUMENT.getCode());
            return MessageStatus.FAILURE;
        }
        // add error code
        message.addAttribute(RESP_ERROR_CODE, ErrorCode.UNKNOW_EXCEPTION.getCode());
        return MessageStatus.FAILURE;
    }

    /**
     * handleBusinessException
     * <p>
     * 处理业务异常，某部分状态码，可以重试，但是，得先定义清楚：哪些错误码
     *
     * @param bex
     * @param message
     * @return
     */
    private MessageStatus handleBusinessException(BusinessException bex, QueueMessage message) {
        ErrorCode errorCode = bex.getErrorCode();
        String key = message.getAttribute(QueueMessage.MSG_KEY, String.class);
        String clientId = message.getAttribute(QueueMessage.CLIENT_ID, String.class);
        Long offset = message.getAttribute(QueueMessage.OFFSET, Long.class);
        // add error code
        message.addAttribute(RESP_ERROR_CODE, errorCode.getCode());
        if (errorCode == ErrorCode.CLIENT_PARAM_MISS || errorCode == ErrorCode.ILLEGAL_ARGUMENT) {
            // log for detail something
            LOG.warn(bex, Pair.of("clazz", "BajieConsumerExceptionResolver"),
                Pair.of("method", "handleBusinessException"), Pair.of("group", message.getGroup()),
                Pair.of("partition", message.getPartition()), Pair.of("topic", message.getTopic()),
                Pair.of("message", message.getMessage()), Pair.of("key", key), Pair.of("clientId", clientId),
                Pair.of("offset", offset), Pair.of("error_code", errorCode.getCode()));
        } else {
            // log for detail something
            LOG.error(bex, Pair.of("clazz", "BajieConsumerExceptionResolver"),
                Pair.of("method", "handleBusinessException"), Pair.of("group", message.getGroup()),
                Pair.of("partition", message.getPartition()), Pair.of("topic", message.getTopic()),
                Pair.of("message", message.getMessage()), Pair.of("key", key), Pair.of("clientId", clientId),
                Pair.of("offset", offset), Pair.of("error_code", errorCode.getCode()));
        }
        return MessageStatus.FAILURE;
    }
}
