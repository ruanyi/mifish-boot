package com.ruanyi.mifish.common.model;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2020-08-01 17:46
 */
public class IdAndNameTest {

    @Test
    public void test1() {
        IdAndName idAndName = IdAndName.of(1, "ruanyi");
        Assert.assertEquals(1, idAndName.getId());
        Assert.assertTrue(StringUtils.equals("ruanyi", idAndName.getName()));
    }
}
