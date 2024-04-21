package com.ruanyi.mifish.common.cipher;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-04-21 18:12
 */
public class OpenApiHelperTest {

    @Test
    public void test1() {
        String apiSecret = OpenApiHelper.generate32ApiSecret();
        System.out.println(apiSecret);
        Assert.assertTrue(StringUtils.length(apiSecret) == 32);
    }

    @Test
    public void test2() {
        String apiKey = OpenApiHelper.generate32ApiKey();
        System.out.println(apiKey);
        Assert.assertTrue(StringUtils.length(apiKey) == 32);
    }

    @Test
    public void test3() {
        String apiSecret = OpenApiHelper.generateApiSecret(24);
        System.out.println(apiSecret);
        Assert.assertTrue(StringUtils.length(apiSecret) == 24);
    }

    @Test
    public void test4() {
        String apiKey = OpenApiHelper.generateApiKey(18);
        System.out.println(apiKey);
        Assert.assertTrue(StringUtils.length(apiKey) == 18);
    }
}
