package com.ruanyi.mifish.common.fns.monad;

import com.ruanyi.mifish.common.fns.Fn1;

/**
 * Description:
 *
 * @author: ruanyi Date: 2017-11-05 18:36
 */
public interface Monad<M, T> {

    /**
     * chain
     *
     * @param f
     * @param <RA>
     * @param <R>
     * @return
     */
    <RA, R extends Monad<M, RA>> R chain(Fn1<T, R> f);
}
