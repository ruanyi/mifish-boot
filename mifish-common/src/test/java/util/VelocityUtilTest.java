package util;

import org.junit.Test;

/**
 * Description:
 *
 * @author: rls
 * Date: 2017-12-14 14:02
 */
public class VelocityUtilTest {

    @Test
    public void testExecute() {
        String rs = VelocityUtil.execute("src/test/processor-context.xml");
        System.out.println(rs);
    }
}