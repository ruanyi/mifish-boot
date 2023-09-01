package com.ruanyi.mifish.image;

import org.junit.Assert;
import org.junit.Test;

import com.ruanyi.mifish.image.impl.ImageMagicResizeServiceImpl;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-08-31 18:26
 */
public class ImageMagicResizeServiceImplTest {

    private static final ImageMagicResizeService imageMagicResizeService = new ImageMagicResizeServiceImpl();

    @Test
    public void test() {
        String fromImagePath = "/Users/rls/Documents/tmp/v19/封面图/1129-04-02.JPEG";
        String toImagePath = "/Users/rls/Documents/tmp/v19/img_rs/1129-04-02_1080.JPEG";
        boolean isSuccess = imageMagicResizeService.resize1080(fromImagePath, toImagePath);
        Assert.assertTrue(isSuccess);
    }
}
