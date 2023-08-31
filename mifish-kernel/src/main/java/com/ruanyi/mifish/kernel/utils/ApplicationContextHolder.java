package com.ruanyi.mifish.kernel.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2020-07-26 12:58
 */
public class ApplicationContextHolder implements ApplicationContextAware {

    /**
     * applicationContext
     */
    private static ApplicationContext applicationContext;

    /**
     * getBean
     *
     * @param name
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name) {
        if (applicationContext == null) {
            return null;
        }
        return (T)applicationContext.getBean(name);
    }

    /**
     * getBean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        if (applicationContext == null) {
            return null;
        }
        String[] names = applicationContext.getBeanNamesForType(clazz);
        if (names == null || names.length == 0) {
            return null;
        }
        return applicationContext.getBean(names[0], clazz);
    }

    /**
     * getBeans
     *
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getBeans(Class<T> clazz) {
        List<T> ret = new ArrayList<>();
        if (applicationContext == null) {
            return ret;
        }
        String[] names = applicationContext.getBeanNamesForType(clazz);
        if (names == null || names.length == 0) {
            return ret;
        }
        for (String name : names) {
            ret.add(applicationContext.getBean(name, clazz));
        }
        return ret;
    }

    /**
     * getApplicationContext
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return ApplicationContextHolder.applicationContext;
    }

    /**
     * setApplicationContext
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.applicationContext = applicationContext;
    }

    /**
     * initApplicationContext
     *
     * @param applicationContext
     */
    public synchronized static void initApplicationContext(ApplicationContext applicationContext) {
        ApplicationContextHolder.applicationContext = applicationContext;
    }
}