package com.ruanyi.mifish.env;

import org.junit.Test;

import com.ruanyi.mifish.model.TmpWorkEnv;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-02 18:45
 */
public class LocalTmpWorkEnvTest {

    @Test
    public void test1() {
        TmpWorkEnv tmpWorkEnv = LocalTmpWorkEnv.prepareTmpEnv();
        System.out.println(tmpWorkEnv.getWorkDir());
        LocalTmpWorkEnv.cleanTmpEnv(tmpWorkEnv);
    }
}
