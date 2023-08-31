package com.ruanyi.mifish.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.ruanyi.mifish.common.annotation.OpenApi;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.runtime.InvokerHelper;


import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.Script;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2018-01-19 16:10
 */
@OpenApi(scene = "脚本型场景")
public final class GroovyUtil {

    /**GROOVY_CLASS_LOADER*/
    private static final GroovyClassLoader GROOVY_CLASS_LOADER = new GroovyClassLoader();

    /**
     * LRU_CACHE_CAPACITY
     */
    private static final Integer LRU_CACHE_CAPACITY = 1024;

    /**
     * LRU_GROOVY_CLASS_CACHE
     */
    private static final LinkedHashMap<String, Class<?>> LRU_GROOVY_CLASS_CACHE =
            new LinkedHashMap<String, Class<?>>(LRU_CACHE_CAPACITY, 0.75f, true) {

                private static final long serialVersionUID = 678140127551460915L;

                @Override
                protected boolean removeEldestEntry(Map.Entry eldest) {
                    return size() > LRU_CACHE_CAPACITY;
                }
            };

    /**
     * LOCK
     */
    private static final Lock LOCK = new ReentrantLock();

    /**
     * parseClass
     *
     * @param scriptStr
     * @return
     */
    public static Class<?> parseClass(String scriptStr) {
        if (StringUtils.isBlank(scriptStr)) {
            throw new GroovyRuntimeException("scriptStr[" + scriptStr + "] is blank!");
        }
        try {
            Class<?> clazz = LRU_GROOVY_CLASS_CACHE.get(scriptStr);
            if (clazz == null) {
                LOCK.lock();
                try {
                    clazz = LRU_GROOVY_CLASS_CACHE.get(scriptStr);
                    if (clazz == null) {
                        clazz = GROOVY_CLASS_LOADER.parseClass(scriptStr);
                        LRU_GROOVY_CLASS_CACHE.put(scriptStr, clazz);
                    }
                } finally {
                    LOCK.unlock();
                }
            }
            return clazz;
        } catch (Exception ex) {
            throw new GroovyRuntimeException("scriptStr[" + scriptStr + "] compile error!", ex);
        }
    }

    /**
     * executeScript
     *
     * @param scriptStr
     * @param params
     * @return
     */
    public static Object executeScript(String scriptStr, Map<String, Object> params) throws Exception {
        Class<?> clazz = parseClass(scriptStr);
        if (clazz == null) {
            throw new GroovyRuntimeException("scriptStr[" + scriptStr + "] compile error!");
        }
        Binding binding = new Binding();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                binding.setVariable(entry.getKey(), entry.getValue());
            }
        }
        Script script = InvokerHelper.createScript(clazz, binding);
        script.setBinding(binding);
        return script.run();
    }

    /**
     * GroovyUtil
     */
    private GroovyUtil() {

    }
}
