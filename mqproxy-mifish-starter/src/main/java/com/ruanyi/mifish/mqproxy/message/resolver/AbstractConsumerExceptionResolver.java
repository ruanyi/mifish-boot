package com.ruanyi.mifish.mqproxy.message.resolver;

import static com.ruanyi.mifish.common.utils.Constants.RESP_DETAIL_ERROR_CODE;
import static com.ruanyi.mifish.common.utils.Constants.RESP_ERROR_CODE;

import org.apache.commons.lang3.tuple.Pair;

import com.ruanyi.mifish.common.ex.MissingRequestParameterException;
import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.mqproxy.message.ConsumerExceptionResolver;
import com.ruanyi.mifish.mqproxy.model.MessageStatus;
import com.ruanyi.mifish.mqproxy.model.QueueMessage;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2019-03-29 11:27
 */
public abstract class AbstractConsumerExceptionResolver implements ConsumerExceptionResolver {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /**
     * 统一处理其他异常
     *
     * @param th 异常
     * @param message
     * @return
     */
    public MessageStatus handleException(Throwable th, QueueMessage message) {
        // add error code
        message.addAttribute(RESP_ERROR_CODE, "UNCAUGHT_EXCEPTION");
        message.addAttribute(RESP_DETAIL_ERROR_CODE, "UNCAUGHT_EXCEPTION");
        LOG.error(th, Pair.of("clazz", "ConsumerExceptionResolver"), Pair.of("method", "handleException"),
            Pair.of("group", message.getGroup()), Pair.of("partition", message.getPartition()),
            Pair.of("topic", message.getTopic()), Pair.of("message", message.getMessage()),
            Pair.of("key", message.getAttribute(QueueMessage.MSG_KEY, String.class)),
            Pair.of("clientId", message.getAttribute(QueueMessage.CLIENT_ID, String.class)),
            Pair.of("offset", message.getAttribute(QueueMessage.OFFSET, Long.class)));
        return MessageStatus.SYS_EX;
    }

    /**
     * handleMissingRequestParameterException
     *
     * @param ex
     * @param message
     * @return
     */
    public MessageStatus handleMissingRequestParameterException(MissingRequestParameterException ex,
        QueueMessage message) {
        LOG.error(ex, Pair.of("clazz", "ConsumerExceptionResolver"),
            Pair.of("method", "handleMissingRequestParameterException"), Pair.of("group", message.getGroup()),
            Pair.of("partition", message.getPartition()), Pair.of("topic", message.getTopic()),
            Pair.of("message", message.getMessage()),
            Pair.of("key", message.getAttribute(QueueMessage.MSG_KEY, String.class)),
            Pair.of("clientId", message.getAttribute(QueueMessage.CLIENT_ID, String.class)),
            Pair.of("offset", message.getAttribute(QueueMessage.OFFSET, Long.class)));
        // add error code
        message.addAttribute(RESP_ERROR_CODE, "PARAM_VALUE_MISS");
        message.addAttribute(RESP_DETAIL_ERROR_CODE, "PARAM_VALUE_MISS");
        return MessageStatus.FAILURE;
    }

    /**
     * handleIllegalArgumentException
     *
     * @param ex
     * @param message
     * @return
     */
    public MessageStatus handleIllegalArgumentException(IllegalArgumentException ex, QueueMessage message) {
        LOG.error(ex, Pair.of("clazz", "ConsumerExceptionResolver"),
            Pair.of("method", "handleIllegalArgumentException"), Pair.of("group", message.getGroup()),
            Pair.of("partition", message.getPartition()), Pair.of("topic", message.getTopic()),
            Pair.of("message", message.getMessage()),
            Pair.of("key", message.getAttribute(QueueMessage.MSG_KEY, String.class)),
            Pair.of("clientId", message.getAttribute(QueueMessage.CLIENT_ID, String.class)),
            Pair.of("offset", message.getAttribute(QueueMessage.OFFSET, Long.class)));
        // add error code
        message.addAttribute(RESP_ERROR_CODE, "PARAM_VALUE_INVALID");
        message.addAttribute(RESP_DETAIL_ERROR_CODE, "PARAM_VALUE_INVALID");
        return MessageStatus.FAILURE;
    }

    /**
     * handleClassCastException
     *
     * @param ex
     * @param message
     * @return
     */
    public MessageStatus handleClassCastException(ClassCastException ex, QueueMessage message) {
        LOG.error(ex, Pair.of("clazz", "ConsumerExceptionResolver"), Pair.of("method", "handleClassCastException"),
            Pair.of("group", message.getGroup()), Pair.of("partition", message.getPartition()),
            Pair.of("topic", message.getTopic()), Pair.of("message", message.getMessage()),
            Pair.of("key", message.getAttribute(QueueMessage.MSG_KEY, String.class)),
            Pair.of("clientId", message.getAttribute(QueueMessage.CLIENT_ID, String.class)),
            Pair.of("offset", message.getAttribute(QueueMessage.OFFSET, Long.class)));
        // add error code
        message.addAttribute(RESP_ERROR_CODE, "PARAM_VALUE_INVALID");
        message.addAttribute(RESP_DETAIL_ERROR_CODE, "PARAM_VALUE_INVALID");
        return MessageStatus.FAILURE;
    }
}
