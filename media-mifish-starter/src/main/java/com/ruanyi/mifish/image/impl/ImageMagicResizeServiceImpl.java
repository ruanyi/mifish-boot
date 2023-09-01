package com.ruanyi.mifish.image.impl;

import org.springframework.stereotype.Service;

import com.ruanyi.mifish.common.model.ProcessResult;
import com.ruanyi.mifish.common.utils.MutiProcessUtil;
import com.ruanyi.mifish.common.utils.UUIDUtil;
import com.ruanyi.mifish.image.ImageMagicResizeService;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-08-31 18:22
 */
@Service
public class ImageMagicResizeServiceImpl implements ImageMagicResizeService {

    /** CMD */
    private static final String CMD = "/usr/local/bin/convert -resize 1920x1080! %s %s";

    /**
     * @see ImageMagicResizeService#resize1080(String, String)
     */
    @Override
    public boolean resize1080(String fromImagePath, String toImagePath) {
        String uuid = UUIDUtil.obtainUUID();
        String cmd = String.format(CMD, fromImagePath, toImagePath);
        ProcessResult pr = MutiProcessUtil.runShellProcess(uuid, cmd);
        return pr.isSuccess();
    }
}
