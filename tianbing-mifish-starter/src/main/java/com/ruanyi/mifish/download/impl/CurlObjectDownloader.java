package com.ruanyi.mifish.download.impl;

import com.ruanyi.mifish.common.model.ProcessResult;
import com.ruanyi.mifish.common.utils.MutiProcessUtil;
import com.ruanyi.mifish.common.utils.UUIDUtil;
import com.ruanyi.mifish.download.ObjectDownloader;
import com.ruanyi.mifish.kernel.model.uri.DownloadURI;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-03 10:56
 */
public class CurlObjectDownloader implements ObjectDownloader {

    /** CMD */
    private static final String CMD = "curl \"%s\" -o %s";

    /**
     * @see ObjectDownloader#download(DownloadURI, String)
     */
    @Override
    public boolean download(DownloadURI downloadURI, String savesPath) {
        // todo 将object uri转变成可供下载的对象url，该url直接用于下载，能够有效低降低下载的公网带宽成本
        String downloadUrl = downloadURI.getUrl();
        String uuid = UUIDUtil.obtainUUID();
        String cmd = String.format(CMD, downloadUrl, savesPath);
        ProcessResult pr = MutiProcessUtil.runShellProcess(uuid, cmd);
        return pr.isSuccess();
    }

    /**
     * @see ObjectDownloader#getCloud()
     */
    @Override
    public String getCloud() {
        return "curl";
    }
}
