package com.ruanyi.mifish.common.convertor;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2017-11-05 18:03
 */
public interface Convertor<S, R> {

    /**
     * convert
     *
     * @param source
     * @return
     */
    R convert(S source);
}
