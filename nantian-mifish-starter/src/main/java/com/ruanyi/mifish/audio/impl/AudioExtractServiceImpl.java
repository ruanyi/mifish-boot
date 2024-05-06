package com.ruanyi.mifish.audio.impl;

import org.springframework.stereotype.Service;

import com.ruanyi.mifish.audio.AudioExtractService;
import com.ruanyi.mifish.common.model.ProcessResult;
import com.ruanyi.mifish.common.utils.MutiProcessUtil;
import com.ruanyi.mifish.common.utils.UUIDUtil;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-09-09 10:00
 */
@Service
public class AudioExtractServiceImpl implements AudioExtractService {

    /** CMD */
    private static final String CMD =
        "/usr/local/bin/ffmpeg -i {0} -vn -y -acodec libfdk_aac -copyts {1} -loglevel error";

    /**
     * @see AudioExtractService#extract(String, String)
     */
    @Override
    public boolean extract(String fromVideoPath, String saveasPath) {
        String uuid = UUIDUtil.obtainUUID();
        String cmd = String.format(CMD, fromVideoPath, saveasPath);
        ProcessResult pr = MutiProcessUtil.runShellProcess(uuid, cmd);
        return pr.isSuccess();
    }
}
