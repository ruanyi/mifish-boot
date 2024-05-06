package com.ruanyi.mifish.image;

import org.junit.Test;

import com.ruanyi.mifish.image.impl.JpegoptimCompressServiceImpl;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-09-01 14:37
 */
public class JpegoptimCompressServiceImplTest {

    private static final JpegoptimCompressServiceImpl jpegoptimCompressService = new JpegoptimCompressServiceImpl();

    @Test
    public void test2() {
        String fromImagePath = "/Users/ruanyi/Documents/tmp/v19/封面图/1129-04-02.JPEG";
        String outDir = "/Users/ruanyi/Documents/tmp/v19/img_rs";
        jpegoptimCompressService.compress(50, fromImagePath, outDir);
    }
}
