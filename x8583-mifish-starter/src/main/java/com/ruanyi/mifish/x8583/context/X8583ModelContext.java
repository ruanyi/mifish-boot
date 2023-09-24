package com.ruanyi.mifish.x8583.context;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-19 23:59
 */
public final class X8583ModelContext {

    /** LRU_CACHE_CAPACITY */
    private static final Integer LRU_CACHE_CAPACITY = 1024;

    /** continer */
    private static final LinkedHashMap<Class<?>, ModelAnnotations> CONTINER =
        new LinkedHashMap<Class<?>, ModelAnnotations>(LRU_CACHE_CAPACITY, 0.75f, true) {

            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > LRU_CACHE_CAPACITY;
            }
        };

    /** LOCK */
    private Lock LOCK = new ReentrantLock();

    /**
     * getModelAnnotations
     * 
     * @param clazz
     * @return
     */
    public ModelAnnotations getModelAnnotations(Class<?> clazz) {
        if (CONTINER.containsKey(clazz)) {
            return CONTINER.get(clazz);
        } else {
            LOCK.lock();
            try {
                if (CONTINER.containsKey(clazz)) {
                    return CONTINER.get(clazz);
                }
                ModelAnnotations modelAnnotations = ModelAnnotations.of(clazz);
                CONTINER.put(clazz, modelAnnotations);
                return modelAnnotations;
            } finally {
                LOCK.unlock();
            }
        }
    }

    /**
     * getInstance
     * 
     * @return
     */
    public static X8583ModelContext getInstance() {
        return X8583ModelContextHolder.INSTANCE;
    }

    /** X8583ModelContext */
    private X8583ModelContext() {

    }

    /**
     * Description:
     *
     * @author: ruanyi
     * @Date: 2023-09-19 23:59
     */
    private static class X8583ModelContextHolder {

        private static final X8583ModelContext INSTANCE = new X8583ModelContext();

        /** X8583ModelContextHolder */
        private X8583ModelContextHolder() {

        }
    }
}
