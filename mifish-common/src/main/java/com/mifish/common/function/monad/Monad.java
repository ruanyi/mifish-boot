package com.mifish.common.function.monad;

import com.mifish.common.function.Fn1;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-05 18:36
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
