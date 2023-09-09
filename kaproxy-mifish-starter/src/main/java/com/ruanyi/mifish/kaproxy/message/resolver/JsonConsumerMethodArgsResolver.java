package com.ruanyi.mifish.kaproxy.message.resolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;

import com.ruanyi.mifish.common.ex.MissingRequestParameterException;
import com.ruanyi.mifish.common.utils.JacksonUtils;
import com.ruanyi.mifish.kaproxy.container.KaproxyConsumerMetaContainer;
import com.ruanyi.mifish.kaproxy.message.ConsumerMethodArgsResolver;
import com.ruanyi.mifish.kaproxy.model.KaproxyConsumerMeta;
import com.ruanyi.mifish.kaproxy.model.QueueMessage;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2018-09-13 20:49
 */
public class JsonConsumerMethodArgsResolver implements ConsumerMethodArgsResolver {

    /** consumerMetaContainer */
    private KaproxyConsumerMetaContainer kaproxyConsumerMetaContainer = KaproxyConsumerMetaContainer.getInstance();

    /**
     * @see ConsumerMethodArgsResolver#resolveArguments(KaproxyConsumerMeta, QueueMessage)
     */
    @Override
    public Object[] resolveArguments(KaproxyConsumerMeta kaproxyConsumerMeta, QueueMessage message) throws Exception {
        Method method = kaproxyConsumerMeta.getMethod();
        Parameter[] params = method.getParameters();
        // 假如没有参数
        if (params.length < 1) {
            return new Object[] {};
        }
        Object[] args = new Object[params.length];
        // 暂不支持list，从目前来看没有list的需求
        Map<String, Object> jsonValues = JacksonUtils.json2Map(message.getMessage(), Object.class);
        // 处理入队列时间
        if (jsonValues.containsKey(QueueMessage.EN_QUEUE_TIME)) {
            message.setEnqueueTimeMillis((Long)jsonValues.get(QueueMessage.EN_QUEUE_TIME));
        }
        // 处理消息ID
        if (jsonValues.containsKey(QueueMessage.MESSAGE_ID)) {
            message.setMessageId((String)jsonValues.get(QueueMessage.MESSAGE_ID));
        }
        // 处理具体的方法入参
        for (int i = 0, len = params.length; i < len; i++) {
            Parameter param = params[i];
            RequestParam requestParam = param.getAnnotation(RequestParam.class);
            if (QueueMessage.class.isAssignableFrom(param.getType())) {
                args[i] = message;
            } else if (requestParam != null) {
                Class<?> type = param.getType();
                Object v = jsonValues
                    .get(StringUtils.isBlank(requestParam.value()) ? requestParam.name() : requestParam.value());
                if (v == null) {
                    v = requestParam.defaultValue();
                }
                if (isMissingRequestParameter(requestParam, v)) {
                    throw new MissingRequestParameterException(param.getName(), param.getType().getName());
                }
                // type.cast(v)直接强转 有些格式需要显示的做转换 先处理 Integer-->Long, 其他后续再处理
                if (Long.class.isAssignableFrom(type) && v instanceof Integer) {
                    args[i] = ((Integer)v).longValue();
                } else {
                    args[i] = type.cast(v);
                }
            } else {
                // 需支持泛型序列化
                Type type = param.getParameterizedType();
                Object v = JacksonUtils.json2Obj(message.getMessage(), type);
                args[i] = v;
            }
        }
        return args;
    }

    /**
     * 判断是否参数缺失
     *
     * @param requestParam
     * @param requestValue
     * @return
     */
    private boolean isMissingRequestParameter(RequestParam requestParam, Object requestValue) {
        return requestParam.required()
            && (requestValue == null || StringUtils.equals(requestValue + "", ValueConstants.DEFAULT_NONE));
    }
}
