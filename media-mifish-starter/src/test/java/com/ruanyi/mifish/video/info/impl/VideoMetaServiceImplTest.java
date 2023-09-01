package com.ruanyi.mifish.video.info.impl;

import org.junit.Assert;
import org.junit.Test;

import com.ruanyi.mifish.model.AvInfo;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-08-31 14:31
 */
public class VideoMetaServiceImplTest {

    private final static VideoMetaServiceImpl videoMetaService = new VideoMetaServiceImpl();

    @Test
    public void test1() {
        String localVideoPath = "/Users/ruanyi/Documents/tmp/v19/素材/933-11-02.mp4";
        AvInfo avInfo = videoMetaService.obtainVideoMetaByFFprobe(localVideoPath);
        System.out.println(avInfo.getMediaInfo());
        Assert.assertFalse(avInfo.isEmpty());
    }
}
