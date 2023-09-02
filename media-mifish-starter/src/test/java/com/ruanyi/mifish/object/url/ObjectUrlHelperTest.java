package com.ruanyi.mifish.object.url;

import org.junit.Assert;
import org.junit.Test;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-09-02 18:25
 */
public class ObjectUrlHelperTest {

    @Test
    public void test1() {
        // 模拟的数据
        String url = "http://vdk.data.com/lab_video/64f30b727a5808pea8zm8e3252.mov";
        String suffix = ObjectUrlHelper.parseFileSuffix(url);
        String key = ObjectUrlHelper.parseLastKey(url);
        Assert.assertEquals(suffix, "mov");
        Assert.assertEquals(key, "64f30b727a5808pea8zm8e3252.mov");
        url = "http://vdk.data.com/lab_video/64f30b727a5808pea8zm8e3252.mov?e=1672323232434123414&token=ssfafkwenk";
        suffix = ObjectUrlHelper.parseFileSuffix(url);
        Assert.assertEquals(suffix, "mov");
        key = ObjectUrlHelper.parseLastKey(url);
        Assert.assertEquals(key, "64f30b727a5808pea8zm8e3252.mov");
    }
}
