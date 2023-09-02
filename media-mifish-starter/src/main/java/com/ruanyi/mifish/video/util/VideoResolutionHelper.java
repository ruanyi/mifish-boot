package com.ruanyi.mifish.video.util;

import com.ruanyi.mifish.model.AvInfo;
import com.ruanyi.mifish.model.VideoResolution;

/**
 * Description:
 * 
 * 视频分辨率默认解析工具
 *
 * @author: ruanyi
 * @Date: 2023-09-02 22:21
 */
public final class VideoResolutionHelper {

    /**
     * parseByAvInfo
     * 
     * @param avInfo
     * @return
     */
    public static VideoResolution parseByAvInfo(AvInfo avInfo) {
        if (avInfo == null || avInfo.isEmpty()) {
            return VideoResolution.unknow;
        }
        return obtainVideoResolution(avInfo.getVideoWidth(), avInfo.getVideoHeight());
    }

    /**
     * obtainByShortEdge
     *
     * @param width
     * @param height
     * @return
     */
    public static VideoResolution obtainVideoResolution(Integer width, Integer height) {
        int shortEdge = obtainShortEdge(width, height);
        // 默认走720p
        if (shortEdge <= VideoResolution.unknow.getMaxShortEdge()) {
            return VideoResolution.unknow;
        }
        if (shortEdge <= VideoResolution._480p.getMaxShortEdge()) {
            return VideoResolution._480p;
        }
        if (shortEdge <= VideoResolution._540p.getMaxShortEdge()) {
            return VideoResolution._540p;
        }
        if (shortEdge <= VideoResolution._720p.getMaxShortEdge()) {
            return VideoResolution._720p;
        }
        if (shortEdge <= VideoResolution._1080p.getMaxShortEdge()) {
            return VideoResolution._1080p;
        }
        if (shortEdge <= VideoResolution._2k.getMaxShortEdge()) {
            return VideoResolution._2k;
        }
        if (shortEdge <= VideoResolution._4k.getMaxShortEdge()) {
            return VideoResolution._4k;
        }
        return VideoResolution.bigger_4k;
    }

    /**
     * obtainShortEdnge
     *
     * @param width
     * @param height
     * @return
     */
    public static int obtainShortEdge(Integer width, Integer height) {
        if (width != null && height != null) {
            return Math.min(width, height);
        }
        return -1;
    }

    /** forbit init */
    private VideoResolutionHelper() {

    }
}
