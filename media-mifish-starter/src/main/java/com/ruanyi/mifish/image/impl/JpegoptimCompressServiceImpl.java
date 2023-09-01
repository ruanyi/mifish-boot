package com.ruanyi.mifish.image.impl;

import org.springframework.stereotype.Service;

import com.ruanyi.mifish.common.model.ProcessResult;
import com.ruanyi.mifish.common.utils.MutiProcessUtil;
import com.ruanyi.mifish.common.utils.UUIDUtil;
import com.ruanyi.mifish.image.JpegoptimCompressService;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-01 14:30
 */
@Service
public class JpegoptimCompressServiceImpl implements JpegoptimCompressService {

    /** CMD */
    private static final String CMD = "/usr/local/bin/jpegoptim -d %s -m%s -p %s";

    /**
     * @see JpegoptimCompressService#compress(int, String, String)
     */
    @Override
    public boolean compress(int quality, String fromImagePath, String outDir) {
        String uuid = UUIDUtil.obtainUUID();
        String cmd = String.format(CMD, outDir, quality + "", fromImagePath);
        ProcessResult pr = MutiProcessUtil.runShellProcess(uuid, cmd);
        return pr.isSuccess();
    }
}
