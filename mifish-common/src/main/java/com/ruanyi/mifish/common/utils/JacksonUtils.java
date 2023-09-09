package com.ruanyi.mifish.common.utils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.ruanyi.mifish.common.logs.MifishLogs;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2018-08-22 14:24
 */
public final class JacksonUtils {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /**
     * XML To Object
     *
     * @param xmlContent
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T xmlToBean(String xmlContent, Class<T> cls) {
        try {
            XmlMapper xml = JacksonMapper.getXmlMapper();
            T obj = xml.readValue(xmlContent, cls);
            return obj;
        } catch (Exception ex) {
            LOG.error(Pair.of("clazz", "JacksonUtils"), Pair.of("method", "xmlToBean"),
                Pair.of("xmlContent", xmlContent), Pair.of("msg", "xmlToBean error,return null"));
            return null;
        }
    }

    /**
     * beanToXml
     *
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> String beanToXml(T bean) {
        try {
            XmlMapper xml = JacksonMapper.getXmlMapper();
            String string = xml.writeValueAsString(bean);
            return string;
        } catch (Exception ex) {
            LOG.error(Pair.of("clazz", "JacksonUtils"), Pair.of("method", "beanToXml"),
                Pair.of("msg", "beanToXml error,return null"));
            return null;
        }
    }

    /**
     * toJSONString
     *
     * @param obj
     * @return
     */
    public static String toJSONString(Object obj) {
        try {
            ObjectMapper objectMapper = JacksonMapper.getObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            LOG.error(e, Pair.of("clazz", "JackJsonUtil"), Pair.of("method", "toJSONString"),
                Pair.of("status", "exceptions"));
            return "";
        }
    }

    /**
     * json2Obj
     * <p>
     * clazz只能支持简单普通对象：该对象里只有基础的数据类型：string、int、double、long等 如果对象里有复杂的数据类型，不一定能够转回给原生的一模一样，
     * 当为list，map时，并且对象比较复杂的时候，不一定能够转成原生的对象
     *
     * @param message
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T json2Obj(String message, Class<T> clazz) {
        try {
            ObjectMapper objectMapper = JacksonMapper.getObjectMapper();
            return objectMapper.readValue(message, clazz);
        } catch (IOException ex) {
            LOG.error(ex, Pair.of("clazz", "JackJsonUtil"), Pair.of("method", "json2Obj"),
                Pair.of("msg", "json2Object by clazz error"), Pair.of("json", message));
            return null;
        }
    }

    /**
     * json2Obj
     * 
     * @param message
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T json2Obj(String message, Type type) {
        try {
            JavaType javaType = getJavaType(type);
            ObjectMapper objectMapper = JacksonMapper.getObjectMapper();
            return objectMapper.readValue(message, javaType);
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "JackJsonUtil"), Pair.of("method", "json2Obj"),
                Pair.of("msg", "json to object by type error"), Pair.of("json", message));
            return null;
        }
    }

    /**
     * 获取返回类的绑定信息
     *
     * @param type
     * @return
     */
    private static JavaType getJavaType(Type type) {
        // 判断是否带有泛型
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType)type).getActualTypeArguments();
            // 获取泛型类型
            Class rowClass = (Class)((ParameterizedType)type).getRawType();
            JavaType[] javaTypes = new JavaType[actualTypeArguments.length];
            for (int i = 0; i < actualTypeArguments.length; i++) {
                // 泛型也可能带有泛型，递归获取
                javaTypes[i] = getJavaType(actualTypeArguments[i]);
            }
            return TypeFactory.defaultInstance().constructParametricType(rowClass, javaTypes);
        } else {
            // 简单类型直接用该类构建JavaType
            Class<?> cla = (Class<?>)type;
            return TypeFactory.defaultInstance().constructParametricType(cla, new JavaType[0]);
        }
    }

    /**
     * json2List
     *
     * @param message
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> json2List(String message, Class<T> clazz) {
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
            ObjectMapper objectMapper = JacksonMapper.getObjectMapper();
            return objectMapper.readValue(message,
                TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, clazz));
        } catch (IOException ex) {
            LOG.error(ex, Pair.of("clazz", "JackJsonUtil"), Pair.of("method", "json2List"),
                Pair.of("status", "exceptions"));
            return null;
        }
    }

    /**
     * json2Map
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> json2Map(String message, Class<T> clazz) {
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
            ObjectMapper objectMapper = JacksonMapper.getObjectMapper();
            return objectMapper.readValue(message,
                TypeFactory.defaultInstance().constructMapType(HashMap.class, String.class, clazz));
        } catch (IOException ex) {
            LOG.error(ex, Pair.of("clazz", "JackJsonUtil"), Pair.of("method", "json2Map"),
                Pair.of("status", "exceptions"));
            return null;
        }
    }

    /** 禁止实例化 */
    private JacksonUtils() {

    }

    /**
     * Description:
     *
     * @author: ruanyi
     * @Date: 2022-07-01 14:40
     */
    private static class JacksonMapper {

        /** can reuse, share globally */
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        /**
         * can reuse, share globally
         */
        private static final XmlMapper XML_MAPPER = new XmlMapper();

        static {
            OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            XML_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        }

        /**
         * private constructor
         */
        private JacksonMapper() {}

        /**
         * return a ObjectMapper that is singleton
         *
         * @return
         */
        public static ObjectMapper getObjectMapper() {
            return OBJECT_MAPPER;
        }

        /**
         * return a XmlMapper that is singleton
         *
         * @return
         */
        public static XmlMapper getXmlMapper() {
            return XML_MAPPER;
        }
    }
}
