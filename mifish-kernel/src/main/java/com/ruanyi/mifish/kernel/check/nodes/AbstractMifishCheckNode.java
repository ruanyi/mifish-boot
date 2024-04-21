package com.ruanyi.mifish.kernel.check.nodes;

import com.ruanyi.mifish.kernel.check.MifishCheckNode;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-04-21 15:37
 */
public abstract class AbstractMifishCheckNode implements MifishCheckNode {

    /** order */
    private int order;

    /**
     * AbstractMifishAuthNode
     *
     * @param order
     */
    public AbstractMifishCheckNode(int order) {
        this.order = order;
    }

    /**
     * getOrder
     *
     * @return
     */
    @Override
    public int getOrder() {
        return this.order;
    }

    /**
     * setOrder
     *
     * @param order
     */
    public void setOrder(int order) {
        this.order = order;
    }
}
