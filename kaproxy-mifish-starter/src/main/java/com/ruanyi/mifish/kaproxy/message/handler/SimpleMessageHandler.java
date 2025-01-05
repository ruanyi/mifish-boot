package com.ruanyi.mifish.kaproxy.message.handler;

import static com.ruanyi.mifish.common.utils.Constants.RESP_DETAIL_ERROR_CODE;
import static com.ruanyi.mifish.common.utils.Constants.RESP_ERROR_CODE;
import static com.ruanyi.mifish.kaproxy.model.QueueMessage.MSG_INVOKE_TS;
import static com.ruanyi.mifish.kaproxy.model.QueueMessage.MSG_RESOLVE_TS;

import java.lang.reflect.Method;

import org.springframework.util.ReflectionUtils;

import com.ruanyi.mifish.kaproxy.container.KaproxyConsumerMetaContainer;
import com.ruanyi.mifish.kaproxy.message.ConsumerExceptionResolver;
import com.ruanyi.mifish.kaproxy.message.ConsumerMethodArgsResolver;
import com.ruanyi.mifish.kaproxy.message.MessageHandler;
import com.ruanyi.mifish.kaproxy.message.resolver.CommonConsumerExceptionResolver;
import com.ruanyi.mifish.kaproxy.message.resolver.JsonConsumerMethodArgsResolver;
import com.ruanyi.mifish.kaproxy.model.AnnotationConsumerMeta;
import com.ruanyi.mifish.kaproxy.model.MessageStatus;
import com.ruanyi.mifish.kaproxy.model.QueueMessage;

/**
 * Description:
 * <p>
 *
 * @author: ruanyi
 * @Date: 2018-09-04 21:09
 */
public class SimpleMessageHandler implements MessageHandler {

    /** consumerMetaContainer */
    private KaproxyConsumerMetaContainer consumerMetaContainer = KaproxyConsumerMetaContainer.getInstance();

    /** consumerMethodArgsResolver */
    private ConsumerMethodArgsResolver consumerMethodArgsResolver = new JsonConsumerMethodArgsResolver();

    /** consumerExceptionResolver */
    private ConsumerExceptionResolver consumerExceptionResolver = new CommonConsumerExceptionResolver();

    /**
     * @see MessageHandler#handle(QueueMessage)
     */
    @Override
    public MessageStatus handle(QueueMessage message) {
        MessageStatus messageStatus = MessageStatus.SUCCESS;
        try {
            // obtain consumer meta
            AnnotationConsumerMeta annotationConsumerMeta =
                this.consumerMetaContainer.getConsumerMeta(message.getGroup(), message.getTopic());
            Object consumer = annotationConsumerMeta.getConsumer();
            Method method = annotationConsumerMeta.getMethod();
            // 解析消费者方法的参数值
            long startTs = System.currentTimeMillis();
            Object[] args = this.consumerMethodArgsResolver.resolveArguments(annotationConsumerMeta, message);
            long resolveTs = System.currentTimeMillis() - startTs;
            message.addAttribute(MSG_RESOLVE_TS, resolveTs);
            // 标记该方法可以进入
            ReflectionUtils.makeAccessible(method);
            // 校验通过后，具体执行对应的方法
            Object result = method.invoke(consumer, args);
            long invokeTs = System.currentTimeMillis() - resolveTs - startTs;
            message.addAttribute(MSG_INVOKE_TS, invokeTs);
            if (result instanceof MessageStatus) {
                messageStatus = (MessageStatus)result;
            }
            if (messageStatus == MessageStatus.SYS_EX) {
                // add error code
                message.addAttribute(RESP_ERROR_CODE, "RET_INTERNAL");
                message.addAttribute(RESP_DETAIL_ERROR_CODE, "UNCAUGHT_EXCEPTION");
            }
            return messageStatus;
        } catch (Throwable th) {
            return this.consumerExceptionResolver.resolveException(th, message);
        }
    }

    /**
     * @see MessageHandler#setConsumerMethodArgsResolver(ConsumerMethodArgsResolver)
     */
    @Override
    public void setConsumerMethodArgsResolver(ConsumerMethodArgsResolver consumerMethodArgsResolver) {
        this.consumerMethodArgsResolver = consumerMethodArgsResolver;
    }

    /**
     * @see MessageHandler#setConsumerExceptionResolver(ConsumerExceptionResolver)
     */
    @Override
    public void setConsumerExceptionResolver(ConsumerExceptionResolver consumerExceptionResolver) {
        this.consumerExceptionResolver = consumerExceptionResolver;
    }
}
