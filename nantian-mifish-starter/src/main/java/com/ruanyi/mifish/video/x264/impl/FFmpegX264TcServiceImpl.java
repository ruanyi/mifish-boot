package com.ruanyi.mifish.video.x264.impl;

import org.springframework.stereotype.Service;

import com.ruanyi.mifish.common.model.ProcessResult;
import com.ruanyi.mifish.common.utils.MutiProcessUtil;
import com.ruanyi.mifish.common.utils.UUIDUtil;
import com.ruanyi.mifish.kernel.model.info.AvInfo;
import com.ruanyi.mifish.video.x264.FFmpegX264TcService;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-08-31 17:17
 */
@Service
public class FFmpegX264TcServiceImpl implements FFmpegX264TcService {

    /** CMD */
    private static final String CMD =
        "/usr/local/bin/ffmpeg -i %s -s %sx%s -profile:v high444 -c:v libx264 -preset veryslow -crf 25 -r 30/1 -g 120 -keyint_min 30 -sc_threshold 40 -bf 3 -b_strategy 2 -refs 5 -c:a aac -profile:a aac_low -b:a 128k -movflags faststart -max_muxing_queue_size 9999 -f mp4 -loglevel error %s";

    /**
     * @see FFmpegX264TcService#x264CrfTcBySourceMeta(AvInfo, String, String)
     */
    @Override
    public boolean x264CrfTcBySourceMeta(AvInfo sourceMeta, String fromVideoPath, String toVideoPath) {
        String uuid = UUIDUtil.obtainUUID();
        String cmd =
            String.format(CMD, fromVideoPath, sourceMeta.getVideoWidth(), sourceMeta.getVideoHeight(), toVideoPath);
        ProcessResult pr = MutiProcessUtil.runShellProcess(uuid, cmd);
        return pr.isSuccess();
    }

    /**
     * @see FFmpegX264TcService#x264AbrTc(String, String)
     */
    @Override
    public boolean x264AbrTc(String fromVideoPath, String toVideoPath) {
        return false;
    }

    /**
     * @see FFmpegX264TcService#twoPassTc(String, String)
     */
    @Override
    public boolean twoPassTc(String fromVideoPath, String toVideoPath) {
        return false;
    }
}
