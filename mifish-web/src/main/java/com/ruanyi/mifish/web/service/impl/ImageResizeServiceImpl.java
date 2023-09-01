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
import com.ruanyi.mifish.image.ImageMagicResizeService;
import com.ruanyi.mifish.web.service.ImageResizeService;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-08-31 20:15
 */
@Service
public class ImageResizeServiceImpl implements ImageResizeService {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /** imageMagicResizeService */
    @Autowired
    private ImageMagicResizeService imageMagicResizeService;

    /** THREAD_POOL_EXECUTOR */
    private static final ExecutorService THREAD_POOL_EXECUTOR = Executors.newSingleThreadExecutor();

    /**
     * @see ImageResizeService#asyncResize(String, String)
     */
    @Override
    public void asyncResize(String fromImageDir, String toImageDir) {
        if (StringUtils.isBlank(fromImageDir) || StringUtils.isBlank(toImageDir)) {
            throw new BusinessException("fromImageDir or toImageDir is blank", ErrorCode.ILLEGAL_ARGUMENT);
        }
        List<FilePathName> filePathNames = FileUtils.listAllPaths(fromImageDir);
        if (filePathNames == null || filePathNames.isEmpty()) {
            throw new BusinessException("fromImageDir has no files", ErrorCode.ILLEGAL_ARGUMENT);
        }
        THREAD_POOL_EXECUTOR.submit(() -> {
            for (FilePathName filePathName : filePathNames) {
                String fileName = StringUtils.substringBefore(filePathName.getName(), ".");
                String suffix = StringUtils.substringAfter(filePathName.getName(), ".");
                String toImagePath = toImageDir + "/" + fileName + "_1080." + suffix;
                boolean isSuccess = this.imageMagicResizeService.resize1080(filePathName.getPath(), toImagePath);
                // just log some msg
                LOG.warn(Pair.of("clazz", "ImageResizeService"), Pair.of("mehod", "asyncResize"),
                    Pair.of("fromImagePath", filePathName.getPath()), Pair.of("isSuccess", isSuccess),
                    Pair.of("toImagePath", toImagePath));
            }
        });
    }
}
