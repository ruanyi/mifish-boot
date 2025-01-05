package com.ruanyi.mifish.kaproxy.container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.common.utils.JacksonUtils;
import com.ruanyi.mifish.kaproxy.annotation.KaproxyConsumer;
import com.ruanyi.mifish.kaproxy.model.AnnotationConsumerMeta;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-11-17 18:15
 */
public class KaproxyConsumerMetaContainer {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /** parseLists */
    private Map<String, AnnotationConsumerMeta> consumerMetas = new ConcurrentHashMap<>();

    /** KaproxyConsumerMetaContainer */
    private KaproxyConsumerMetaContainer() {

    }

    /**
     * putConsumerMeta
     *
     * @param annotationConsumerMeta
     * @return
     */
    public KaproxyConsumerMetaContainer putConsumerMeta(AnnotationConsumerMeta annotationConsumerMeta) {
        if (annotationConsumerMeta != null) {
            // 一般情况下，不允许出现此类情况,但是，不阻止，只是打印warn日志
            if (this.consumerMetas.containsKey(annotationConsumerMeta.getKey())) {
                LOG.warn(Pair.of("clazz", "KaproxyConsumerMetaContainer"), Pair.of("method", "putConsumerMeta"),
                    Pair.of("message", "consumer meta key is already in container,please check carefully"),
                    Pair.of("key", annotationConsumerMeta.getKey()), Pair.of("all_consumer_metas", this.toString()),
                    Pair.of("consumerMeta_new", annotationConsumerMeta.toString()));
            }
            this.consumerMetas.put(annotationConsumerMeta.getKey(), annotationConsumerMeta);
        }
        return this;
    }

    /**
     * putConsumerMetas
     *
     * @param annotationConsumerMetas
     * @return
     */
    public KaproxyConsumerMetaContainer putConsumerMetas(List<AnnotationConsumerMeta> annotationConsumerMetas) {
        if (annotationConsumerMetas == null || annotationConsumerMetas.isEmpty()) {
            return this;
        }
        for (AnnotationConsumerMeta annotationConsumerMeta : annotationConsumerMetas) {
            putConsumerMeta(annotationConsumerMeta);
        }
        return this;
    }

    /**
     * getConsumerMeta
     *
     * @param key
     * @return
     */
    public AnnotationConsumerMeta getConsumerMeta(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return this.consumerMetas.get(key);
    }

    /**
     * getConsumerMeta
     *
     * @param group
     * @param topic
     * @return
     */
    public AnnotationConsumerMeta getConsumerMeta(String group, String topic) {
        return getConsumerMeta(AnnotationConsumerMeta.buildKey(group, topic));
    }

    /**
     * getMqproxyConsumer
     *
     * @param group
     * @param topic
     * @return
     */
    public KaproxyConsumer getMqproxyConsumer(String group, String topic) {
        AnnotationConsumerMeta annotationConsumerMeta = getConsumerMeta(group, topic);
        if (annotationConsumerMeta != null) {
            return annotationConsumerMeta.getMqproxyConsumer();
        }
        return null;
    }

    @Override
    public String toString() {
        Map<String, String> data = new HashMap<>(this.consumerMetas.size());
        for (Map.Entry<String, AnnotationConsumerMeta> entry : this.consumerMetas.entrySet()) {
            data.put(entry.getKey(), entry.getValue().toString());
        }
        return JacksonUtils.toJSONString(data);
    }

    /**
     * isContainConsumer
     *
     * @param group
     * @param topic
     * @return
     */
    public boolean isContainConsumer(String group, String topic) {
        AnnotationConsumerMeta annotationConsumerMeta = getConsumerMeta(group, topic);
        return annotationConsumerMeta != null;
    }

    /**
     * getInstance
     *
     * @return
     */
    public static KaproxyConsumerMetaContainer getInstance() {
        return ConsumerMetaContainerHolder.INSTANCE;
    }

    /**
     * Description:
     *
     * @author: ruanyi
     * @Date: 2018-09-04 19:46
     */
    private static class ConsumerMetaContainerHolder {

        private static KaproxyConsumerMetaContainer INSTANCE = new KaproxyConsumerMetaContainer();

        private ConsumerMetaContainerHolder() {

        }
    }
}
