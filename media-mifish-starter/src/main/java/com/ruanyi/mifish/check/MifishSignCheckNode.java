package com.ruanyi.mifish.check;

import com.ruanyi.mifish.common.ex.BusinessException;
import com.ruanyi.mifish.kernel.check.MifishCheck;
import com.ruanyi.mifish.kernel.check.MifishCheckChain;
import com.ruanyi.mifish.kernel.check.MifishCheckContext;
import com.ruanyi.mifish.kernel.check.MifishCheckNode;
import com.ruanyi.mifish.kernel.check.nodes.AbstractMifishCheckNode;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-04-21 18:20
 */
public class MifishSignCheckNode extends AbstractMifishCheckNode {

    /**
     * MifishSignCheckNode
     *
     * @param order
     */
    public MifishSignCheckNode(int order) {
        super(order);
    }

    /**
     * @see MifishCheckNode#doNode(MifishCheck, MifishCheckContext, MifishCheckChain)
     */
    @Override
    public void doNode(MifishCheck mifishCheck, MifishCheckContext checkContext, MifishCheckChain checkChain)
        throws BusinessException {

    }
}
