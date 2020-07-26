package chain;

import com.mifish.common.chain.Chain;
import com.mifish.common.chain.Engine;
import com.mifish.common.chain.impl.SimpleChain;
import com.mifish.common.chain.impl.SimpleEngine;
import com.mifish.common.chain.nodes.DummyNode;
import org.junit.Assert;
import org.junit.Test;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-06 13:59
 */
public class SimpleEngineTest {

    @Test
    public void test1() {
        Chain<String, String> chain = SimpleChain.buildSimpleChain(DummyNode.newInstance(), DummyNode.newInstance(),
                DummyNode.newInstance());
        Engine<String> engine = new SimpleEngine<>();
        String rs = engine.digest("1", chain);
        Assert.assertEquals(rs, null);
    }
}
