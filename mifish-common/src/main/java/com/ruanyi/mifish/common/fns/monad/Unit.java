package com.ruanyi.mifish.common.fns.monad;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2017-11-05 18:38
 */
public interface Unit<T, M extends Monad<?, T>> {
    /**
     * unit
     *
     * @param t
     * @return
     */
    M unit(T t);
}
