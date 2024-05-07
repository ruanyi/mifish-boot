package com.ruanyi.mifish.model.env;

import org.junit.Assert;
import org.junit.Test;

import com.ruanyi.mifish.env.LocalTmpWorkEnv;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-09-02 18:45
 */
public class LocalTmpWorkEnvTest {

    @Test
    public void test1() {
        TmpWorkEnv tmpWorkEnv = LocalTmpWorkEnv.prepareTmpEnv();
        System.out.println(tmpWorkEnv.getWorkDir());
        LocalTmpWorkEnv.cleanTmpEnv(tmpWorkEnv);
        Assert.assertNotNull(tmpWorkEnv);
    }
}
