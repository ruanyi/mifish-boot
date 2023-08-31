package com.ruanyi.mifish.video.info.impl;

import org.springframework.stereotype.Service;

import com.ruanyi.mifish.common.model.ProcessResult;
import com.ruanyi.mifish.common.utils.MutiProcessUtil;
import com.ruanyi.mifish.common.utils.UUIDUtil;
import com.ruanyi.mifish.model.AvInfo;
import com.ruanyi.mifish.video.info.VideoMetaService;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-08-31 13:52
 */
@Service
public class VideoMetaServiceImpl implements VideoMetaService {

    /** CMD */
    private static final String CMD =
        "ffprobe -v quiet -print_format json -show_format -show_streams -loglevel error %s";

    /**
     * @see VideoMetaService#obtainVideoMetaByFFprobe(String)
     */
    @Override
    public AvInfo obtainVideoMetaByFFprobe(String videoPath) {
        String uuid = UUIDUtil.obtainUUID();
        String cmd = String.format(CMD, videoPath);
        ProcessResult pr = MutiProcessUtil.runShellProcess(uuid, cmd);
        if (pr.isSuccess()) {
            return AvInfo.from(pr.getStdout());
        }
        return AvInfo.failure(pr.getStderr());
    }
}
