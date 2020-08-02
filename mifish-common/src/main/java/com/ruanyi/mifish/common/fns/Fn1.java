package com.ruanyi.mifish.common.fns;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-10-15 21:56
 */
public interface Fn1<A, R> {

    /**
     * eval
     *
     * @param arg
     * @return
     */
    R eval(A arg);
}
