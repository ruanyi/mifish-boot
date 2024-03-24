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
import com.ruanyi.mifish.common.utils.UUIDUtil;
import com.ruanyi.mifish.env.LocalTmpWorkEnv;
import com.ruanyi.mifish.model.object.TmpWorkEnv;
import com.ruanyi.mifish.model.video.AvInfo;
import com.ruanyi.mifish.object.download.CurlDownloadFileComponent;
import com.ruanyi.mifish.object.url.ObjectUrlHelper;
import com.ruanyi.mifish.video.info.VideoMetaService;
import com.ruanyi.mifish.video.x264.FFmpegX264TcService;
import com.ruanyi.mifish.web.service.VideoX264TcService;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-08-31 18:35
 */
@Service
public class VideoX264TcServiceImpl implements VideoX264TcService {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /** videoMetaService */
    @Autowired
    private VideoMetaService videoMetaService;

    /** fFmpegX264TcService */
    @Autowired
    private FFmpegX264TcService fFmpegX264TcService;

    /** curlDownloadFileComponent */
    @Autowired
    private CurlDownloadFileComponent curlDownloadFileComponent;

    /** THREAD_POOL_EXECUTOR */
    private static final ExecutorService THREAD_POOL_EXECUTOR = Executors.newSingleThreadExecutor();

    /**
     * @see VideoX264TcService#asyncX264Tc(String, String)
     */
    @Override
    public void asyncX264Tc(String fromVideoDir, String toVideoDir) {
        if (StringUtils.isBlank(fromVideoDir) || StringUtils.isBlank(toVideoDir)) {
            throw new BusinessException("fromVideoDir or toVideoDir is blank", ErrorCode.ILLEGAL_ARGUMENT);
        }
        List<FilePathName> filePathNames = FileUtils.listAllPaths(fromVideoDir);
        if (filePathNames == null || filePathNames.isEmpty()) {
            throw new BusinessException("fromVideoDir has no files", ErrorCode.ILLEGAL_ARGUMENT);
        }
        THREAD_POOL_EXECUTOR.submit(() -> {
            for (FilePathName filePathName : filePathNames) {
                AvInfo avInfo = this.videoMetaService.obtainVideoMetaByFFprobe(filePathName.getPath());
                String fileName = StringUtils.substringBefore(filePathName.getName(), ".");
                String suffix = StringUtils.substringAfter(filePathName.getName(), ".");
                String toVideoPath = toVideoDir + "/" + fileName + "_4k." + suffix;
                boolean isSuccess =
                    this.fFmpegX264TcService.x264CrfTcBySourceMeta(avInfo, filePathName.getPath(), toVideoPath);
                // just log some msg
                LOG.warn(Pair.of("clazz", "ImageResizeService"), Pair.of("mehod", "asyncResize"),
                    Pair.of("fromVideoPath", filePathName.getPath()), Pair.of("isSuccess", isSuccess),
                    Pair.of("toVideoPath", toVideoPath));
            }
        });
    }

    /**
     * @see VideoX264TcService#syncX264Tc(String)
     */
    @Override
    public String syncX264Tc(String videoUrl) {
        TmpWorkEnv tmpWorkEnv = LocalTmpWorkEnv.prepareTmpEnv();
        boolean downloadFlag = this.curlDownloadFileComponent.download(videoUrl, tmpWorkEnv.getWorkDir());
        String lastKey = ObjectUrlHelper.parseLastKey(videoUrl);
        String localVideoPath = tmpWorkEnv.getWorkDir() + "/" + lastKey;
        AvInfo avInfo = this.videoMetaService.obtainVideoMetaByFFprobe(localVideoPath);
        String suffix = ObjectUrlHelper.parseFileSuffix(videoUrl);
        String toVideoPath = tmpWorkEnv.getWorkDir() + "/" + UUIDUtil.obtainUUID() + suffix;
        boolean tcFlag = this.fFmpegX264TcService.x264CrfTcBySourceMeta(avInfo, localVideoPath, toVideoPath);
        LocalTmpWorkEnv.cleanTmpEnv(tmpWorkEnv);
        // just log some msg
        LOG.warn(Pair.of("clazz", "ImageResizeService"), Pair.of("mehod", "syncX264Tc"),
            Pair.of("fromVideoPath", localVideoPath), Pair.of("downloadFlag", downloadFlag), Pair.of("tcFlag", tcFlag),
            Pair.of("toVideoPath", toVideoPath));
        return toVideoPath;
    }

    /**
     * @see VideoX264TcService#asyncCheckFrameNums(String)
     */
    @Override
    public void asyncCheckFrameNums(String fromVideoDir) {
        for (int i = 0; i < 57; i++) {
            final int index = i;
            THREAD_POOL_EXECUTOR.submit(() -> {
                AvInfo segmentAvInfo =
                    this.videoMetaService.obtainVideoMetaByFFprobe(fromVideoDir + "/videoSegment_" + index + ".mp4");
                int segmentFrames = segmentAvInfo.getNbFrames();
                AvInfo openApiAvInfo =
                    this.videoMetaService.obtainVideoMetaByFFprobe(fromVideoDir + "/open_api_" + index + ".mp4");
                int openApiFrames = openApiAvInfo.getNbFrames();
                // just log some msg
                LOG.warn(Pair.of("clazz", "ImageResizeService"), Pair.of("mehod", "asyncCheckFrameNums"),
                    Pair.of("segment_video_path", fromVideoDir + "/videoSegment_" + index + ".mp4"),
                    Pair.of("open_api_video_path", fromVideoDir + "/open_api_" + index + ".mp4"),
                    Pair.of("segmentFrames", segmentFrames), Pair.of("openApiFrames", openApiFrames));
            });
        }
    }
}
