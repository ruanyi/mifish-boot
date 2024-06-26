package com.ruanyi.mifish.kernel.check.chain;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import com.ruanyi.mifish.common.ex.BusinessException;
import com.ruanyi.mifish.kernel.check.MifishCheck;
import com.ruanyi.mifish.kernel.check.MifishCheckChain;
import com.ruanyi.mifish.kernel.check.MifishCheckContext;
import com.ruanyi.mifish.kernel.check.MifishCheckNode;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-04-21 15:29
 */
public class SimpleMifishCheckChain implements MifishCheckChain {

    /** iterator */
    private Iterator<MifishCheckNode> iterator;

    /**
     * SimpleMifishAuthChain
     * 
     * @param checkNodes
     */
    public SimpleMifishCheckChain(List<MifishCheckNode> checkNodes) {
        checkArgument(checkNodes != null, "checkNodes cannot be null in SimpleMifishCheckChain");
        this.iterator = checkNodes.iterator();
    }

    /**
     * @see MifishCheckChain#doChain(MifishCheck, MifishCheckContext)
     */
    @Override
    public void doChain(MifishCheck mifishCheck, MifishCheckContext checkContext) throws BusinessException {
        if (this.iterator.hasNext()) {
            MifishCheckNode checkNode = this.iterator.next();
            if (checkNode != null) {
                checkNode.doNode(mifishCheck, checkContext, this);
            }
        }
    }

    /**
     * buildSimpleChain
     *
     * @param checkNodes
     * @return
     */
    public static SimpleMifishCheckChain buildSimpleChain(MifishCheckNode... checkNodes) {
        List<MifishCheckNode> nodeList = Lists.newArrayList(checkNodes);
        Collections.sort(nodeList);
        return new SimpleMifishCheckChain(nodeList);
    }

    /**
     * buildSimpleChain
     *
     * @param checkNodes
     * @return
     */
    public static SimpleMifishCheckChain buildSimpleChain(List<MifishCheckNode> checkNodes) {
        checkArgument(checkNodes != null, "checkNodes cannot be null in SimpleMifishCheckChain");
        Collections.sort(checkNodes);
        return new SimpleMifishCheckChain(checkNodes);
    }
}
