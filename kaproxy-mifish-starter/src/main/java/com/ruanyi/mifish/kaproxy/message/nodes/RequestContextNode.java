package com.ruanyi.mifish.kaproxy.message.nodes;

import static com.ruanyi.mifish.common.utils.Constants.RESP_DETAIL_ERROR_CODE;
import static com.ruanyi.mifish.common.utils.Constants.RESP_ERROR_CODE;
import static com.ruanyi.mifish.kaproxy.model.QueueMessage.*;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.MDC;

import com.ruanyi.mifish.common.context.RequestContext;
import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.common.profiler.MethodProfiler;
import com.ruanyi.mifish.common.utils.UUIDUtil;
import com.ruanyi.mifish.kaproxy.annotation.KaproxyConsumer;
import com.ruanyi.mifish.kaproxy.message.MessageChain;
import com.ruanyi.mifish.kaproxy.message.MessageNode;
import com.ruanyi.mifish.kaproxy.model.KaproxyConsumerMeta;
import com.ruanyi.mifish.kaproxy.model.MessageStatus;
import com.ruanyi.mifish.kaproxy.model.QueueMessage;

/**
 * Description:
 * <p>
 * 定位：类似：RequestContextFilter
 *
 * @author: ruanyi
 * @Date: 2019-04-11 21:40
 */
public class RequestContextNode implements MessageNode {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /** order */
    private int order;

    /**
     * RequestContextNode
     *
     * @param order
     */
    public RequestContextNode(int order) {
        this.order = order;
    }

    /**
     * @see MessageNode#doNode(KaproxyConsumer, QueueMessage, MessageChain)
     */
    @Override
    public MessageStatus doNode(KaproxyConsumer kaproxyConsumer, QueueMessage message, MessageChain chain) {
        try {
            // init trace_id
            String resourceUrl = KaproxyConsumerMeta.buildKey(message.getGroup(), message.getTopic());
            String traceId = UUIDUtil.obtainUUID();
            RequestContext.init(traceId, resourceUrl);
            // MDC放置：trace_id
            MDC.put("trace_id", traceId);
            // 开启方法耗时打印
            MessageStatus msgStatus = chain.doChain(kaproxyConsumer, message);
            // log processor access log
            logProcessorAccessLog(message, msgStatus);
            // 此时，在返回消息状态
            return msgStatus;
        } finally {
            onFinally(message);
        }
    }

    /**
     * 打印处理机的access log日志信息
     * <p>
     * 参考标准：http://cf.meitu.com/confluence/pages/viewpage.action?pageId=56576367 实现
     *
     * @param message
     * @param msgStatus
     */
    private void logProcessorAccessLog(QueueMessage message, MessageStatus msgStatus) {
        if (message == null || msgStatus == null) {
            return;
        }
        if (LOG.isInfoEnabled()) {
            LOG.info(Pair.of("msgId", message.getMessageId()), Pair.of("topic", message.getTopic()),
                Pair.of("partition", message.getPartition()), Pair.of("group", message.getGroup()),
                Pair.of("offset", message.getOffset()), Pair.of("status", msgStatus.name()),
                Pair.of("key", message.getMsgKey()),
                Pair.of("handle_elapse", (System.currentTimeMillis() - message.getDequeueTimeMillis())),
                Pair.of("queue_elapse", (message.getDequeueTimeMillis() - message.getEnqueueTimeMillis())),
                Pair.of("local_queue_elapse", message.getAttribute(LOCAL_QUEUE_ELAPSE, Long.class)),
                Pair.of(RESP_ERROR_CODE, message.getAttribute(RESP_ERROR_CODE, String.class)),
                Pair.of(RESP_DETAIL_ERROR_CODE, message.getAttribute(RESP_DETAIL_ERROR_CODE, String.class)),
                Pair.of("invokedCount", message.getInvokedCount()),
                Pair.of("msg_invoke_ts", message.getAttribute(MSG_INVOKE_TS, Long.class)),
                Pair.of("msg_resolve_ts", message.getAttribute(MSG_RESOLVE_TS, Long.class)));
        }
    }

    /**
     * 做后续处理动作，请确保：不能抛出任何异常
     *
     * @param message
     */
    private void onFinally(QueueMessage message) {
        if (message == null) {
            return;
        }
        // 清理一些线程=上线文的东东
        MethodProfiler.reset();
        // 清理RequestContext的相关信息
        RequestContext.clear();
        // https://www.baeldung.com/mdc-in-log4j-2-logback
        MDC.clear();
    }

    /**
     * getOrder
     *
     * @return
     */
    @Override
    public int getOrder() {
        return this.order;
    }
}
