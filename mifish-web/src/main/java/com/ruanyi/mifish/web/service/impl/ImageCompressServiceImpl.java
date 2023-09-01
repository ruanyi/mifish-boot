package com.ruanyi.mifish.web.service.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruanyi.mifish.common.ex.BusinessException;
import com.ruanyi.mifish.common.ex.ErrorCode;
import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.common.model.FilePathName;
import com.ruanyi.mifish.common.utils.FileUtils;
import com.ruanyi.mifish.image.JpegoptimCompressService;
import com.ruanyi.mifish.web.service.ImageCompressService;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-01 14:33
 */
@Service
public class ImageCompressServiceImpl implements ImageCompressService {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /** jpegoptimCompressService */
    @Autowired
    private JpegoptimCompressService jpegoptimCompressService;

    /** THREAD_POOL_EXECUTOR */
    private static final ExecutorService THREAD_POOL_EXECUTOR = Executors.newSingleThreadExecutor();

    /**
     * @see ImageCompressService#asyncCompress(String, String)
     */
    @Override
    public void asyncCompress(String fromImageDir, String toImageDir) {
        if (StringUtils.isBlank(fromImageDir) || StringUtils.isBlank(toImageDir)) {
            throw new BusinessException("fromImageDir or toImageDir is blank", ErrorCode.ILLEGAL_ARGUMENT);
        }
        List<FilePathName> filePathNames = FileUtils.listAllPaths(fromImageDir);
        if (filePathNames == null || filePathNames.isEmpty()) {
            throw new BusinessException("fromImageDir has no files", ErrorCode.ILLEGAL_ARGUMENT);
        }
        THREAD_POOL_EXECUTOR.submit(() -> {
            for (FilePathName filePathName : filePathNames) {
                boolean isSuccess = this.jpegoptimCompressService.compress(50, filePathName.getPath(), toImageDir);
                // just log some msg
                LOG.warn(Pair.of("clazz", "ImageCompressService"), Pair.of("mehod", "asyncCompress"),
                    Pair.of(
                        "fromImagePath", filePathName.getPath()), Pair.of("isSuccess", isSuccess),
                        Pair.of("toImageDir", toImageDir));
            }
        });
    }
}
