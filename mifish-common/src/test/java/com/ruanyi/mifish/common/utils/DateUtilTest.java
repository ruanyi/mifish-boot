package com.ruanyi.mifish.common.utils;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

/**
 * Description:
 *
 * @author rls
 * @date 2024-12-14 14:14
 */
public class DateUtilTest {

    @Test
    public void testDateToWeek() {
        String str = DateUtil.dateToWeek(new Date());
        System.out.println(str);
        Assert.assertTrue(str.length() == 2);
    }
}
