package profiler;

import com.mifish.common.profiler.MethodProfiler;
import org.junit.Test;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-11-05 21:48
 */
public class MethodProfilerTest {

    @Test
    public void test1() {
        try {
            MethodProfiler.enter("start ===profile log");
            Thread.sleep(1000);
            MethodProfiler.enter("1===method");
            MethodProfiler.release();
            MethodProfiler.release();

            MethodProfiler.enter("2===method");
            MethodProfiler.release();
            MethodProfiler.enter("3===> methods");
            MethodProfiler.release();
            System.out.println(MethodProfiler.dump());
            MethodProfiler.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            MethodProfiler.reset();
        }
    }
}
