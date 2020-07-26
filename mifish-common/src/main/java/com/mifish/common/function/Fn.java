package com.mifish.common.function;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 21:55
 */
public interface Fn<T> {

    /**
     * apply
     *
     * @param args
     * @return
     */
    T apply(Object... args);
}
