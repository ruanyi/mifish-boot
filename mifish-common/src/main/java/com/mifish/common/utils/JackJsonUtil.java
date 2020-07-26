package com.mifish.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.mifish.common.logs.MifishLogs;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2018-08-22 14:24
 */
public final class JackJsonUtil {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /** jackson json */
    private static final ObjectMapper JACKJSON_MAPPER = new ObjectMapper();

    static {
        JACKJSON_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        JACKJSON_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        JACKJSON_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        JACKJSON_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    /**
     * toJSONString
     *
     * @param obj
     * @return
     */
    public static String toJSONString(Object obj) {
        try {
            return JACKJSON_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOG.error(e, Pair.of("clazz", "JackJsonUtil"), Pair.of("method", "toJSONString"),
                Pair.of("status", "exceptions"));
            return "";
        }
    }

    /**
     * parse2Obj
     * <p>
     * clazz只能支持简单普通对象：该对象里只有基础的数据类型：string、int、double、long等 如果对象里有复杂的数据类型，不一定能够转回给原生的一模一样，
     * 当为list，map时，并且对象比较复杂的时候，不一定能够转成原生的对象
     *
     * @param message
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parse2Obj(String message, Class<T> clazz) {
        try {
            return JACKJSON_MAPPER.readValue(message, clazz);
        } catch (IOException e) {
            LOG.error(e, Pair.of("clazz", "JackJsonUtil"), Pair.of("method", "parse2Obj"),
                Pair.of("status", "exceptions"));
            return null;
        }
    }

    /**
     * parse2List
     *
     * @param message
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> parse2List(String message, Class<T> clazz) {
        try {
            if (StringUtils.isEmpty(message)) {
                return null;
            }
            message = message.trim();
            if (!message.startsWith("[")) {
                message = "[" + message;
            }
            if (!message.endsWith("]")) {
                message = message + "]";
            }
            return JACKJSON_MAPPER.readValue(message,
                TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, clazz));
        } catch (IOException ex) {
            LOG.error(ex, Pair.of("clazz", "JackJsonUtil"), Pair.of("method", "parse2List"),
                Pair.of("status", "exceptions"));
            return null;
        }
    }

    /**
     * parse2Map
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> parse2Map(String message, Class<T> clazz) {
        try {
            if (StringUtils.isEmpty(message)) {
                return null;
            }
            message = message.trim();
            if (!message.startsWith("{")) {
                message = "{" + message;
            }
            if (!message.endsWith("}")) {
                message = message + "}";
            }
            return JACKJSON_MAPPER.readValue(message,
                TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, clazz));
        } catch (IOException ex) {
            LOG.error(ex, Pair.of("clazz", "JackJsonUtil"), Pair.of("method", "parse2Map"),
                Pair.of("status", "exceptions"));
            return null;
        }
    }

    /** 禁止实例化 */
    private JackJsonUtil() {

    }
}
