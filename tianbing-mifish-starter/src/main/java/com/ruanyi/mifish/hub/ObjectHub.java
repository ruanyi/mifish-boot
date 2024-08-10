package com.ruanyi.mifish.hub;

import com.ruanyi.mifish.model.fn.FnInput;
import com.ruanyi.mifish.model.fn.FnOutput;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-25 18:25
 */
public interface ObjectHub {

    /**
     * handle
     * 
     * @param fnInput
     * @return
     */
    FnOutput handle(FnInput fnInput);
}
