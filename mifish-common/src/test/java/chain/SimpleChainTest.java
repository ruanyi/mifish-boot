package chain;

import com.mifish.common.chain.Chain;
import com.mifish.common.chain.impl.SimpleChain;
import com.mifish.common.chain.nodes.DummyNode;
import com.mifish.common.profiler.MethodProfiler;
import org.junit.Test;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-02 20:18
 */
public class SimpleChainTest {

    @Test
    public void testDummy() {
        Chain<String, String> chain = SimpleChain.buildSimpleChain(DummyNode.newInstance(), DummyNode.newInstance(),
                DummyNode.newInstance());
        chain.execute("1");
        System.out.println(MethodProfiler.dump());
        MethodProfiler.reset();
        System.out.println();
    }
}
