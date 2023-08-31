package com.ruanyi.mifish.video.x264;

import com.ruanyi.mifish.model.AvInfo;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-08-31 13:49
 */
public interface FFmpegX264TcService {

    /**
     * x264CrfTcBySourceMeta
     *
     * @param sourceAvInfo
     * @param fromVideoPath
     * @param toVideoPath
     * @return
     */
    boolean x264CrfTcBySourceMeta(AvInfo sourceAvInfo, String fromVideoPath, String toVideoPath);
}
