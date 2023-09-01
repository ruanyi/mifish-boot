package com.ruanyi.mifish.common.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2018-09-13 10:49
 */
public class RandomUtilTest {

    @Test
    public void testRandomRange() {
        int index = RandomUtil.randomRange(0, 10);
        System.out.println(index);
        Assert.assertTrue(index <= 10);
        Assert.assertTrue(index >= 0);
        Assert.assertTrue(11 == RandomUtil.randomRange(11, 11));
        Assert.assertTrue(-1 == RandomUtil.randomRange(12, 10));
    }

    @Test
    public void randomSelectOne() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            set.add(RandomUtil.randomSelectOne(list));
        }

        // (1/4)^1000 * C(4,4) 的概率会 fail，真中了可以买彩票
        Assert.assertEquals(set.size(), list.size());
    }

}
