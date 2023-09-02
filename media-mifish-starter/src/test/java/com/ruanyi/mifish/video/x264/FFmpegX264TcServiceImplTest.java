package com.ruanyi.mifish.video.x264;

import org.junit.Test;

import com.ruanyi.mifish.model.video.AvInfo;
import com.ruanyi.mifish.video.info.impl.VideoMetaServiceImpl;
import com.ruanyi.mifish.video.x264.impl.FFmpegX264TcServiceImpl;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-08-31 17:30
 */
public class FFmpegX264TcServiceImplTest {

    private static final FFmpegX264TcServiceImpl ffmpegX264TcService = new FFmpegX264TcServiceImpl();

    private final static VideoMetaServiceImpl videoMetaService = new VideoMetaServiceImpl();

    @Test
    public void test1() {
        String fromVideoPath = "/Users/ruanyi/Documents/tmp/v19/素材/933-11-02.mp4";
        String toVideoPath = "/Users/ruanyi/Documents/tmp/v19/video_rs/933-11-02_4k.mp4";
        AvInfo sourceAvInfo = videoMetaService.obtainVideoMetaByFFprobe(fromVideoPath);
        boolean isSuccess = ffmpegX264TcService.x264CrfTcBySourceMeta(sourceAvInfo, fromVideoPath, toVideoPath);
        System.out.println(isSuccess);
    }
}
