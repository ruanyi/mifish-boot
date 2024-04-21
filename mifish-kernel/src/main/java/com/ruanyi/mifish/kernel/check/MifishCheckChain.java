package com.ruanyi.mifish.kernel.check;

import com.ruanyi.mifish.common.ex.BusinessException;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-04-21 15:21
 */
public interface MifishCheckChain {

    /**
     * doChain
     *
     * @param mifishCheck
     * @param checkContext
     * @return
     * @throws BusinessException
     */
    void doChain(MifishCheck mifishCheck, MifishCheckContext checkContext) throws BusinessException;

}
