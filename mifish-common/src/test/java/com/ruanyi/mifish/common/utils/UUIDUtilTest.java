package com.ruanyi.mifish.common.utils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2020-08-23 21:53
 */
public class UUIDUtilTest {

    @Test
    public void test1() {
        String uuid = UUIDUtil.obtainUUID();
        System.out.println(uuid);
        Assert.assertNotNull(uuid);
    }
}
