package com.ruanyi.mifish.download.impl;

import com.ruanyi.mifish.common.model.ProcessResult;
import com.ruanyi.mifish.common.utils.MutiProcessUtil;
import com.ruanyi.mifish.common.utils.UUIDUtil;
import com.ruanyi.mifish.download.ObjectDownloadComponent;
import com.ruanyi.mifish.kernel.model.uri.ObjectURI;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-03 10:56
 */
public class CurlDownloadComponentImpl implements ObjectDownloadComponent {

    /** CMD */
    private static final String CMD = "curl \"%s\" -o %s";

    /**
     * @see ObjectDownloadComponent#download(ObjectURI, String)
     */
    @Override
    public boolean download(ObjectURI objectURI, String savesPath) {
        // todo 将object uri转变成可供下载的对象url，该url直接用于下载，能够有效低降低下载的公网带宽成本
        String downloadUrl = objectURI.getUrl();
        String uuid = UUIDUtil.obtainUUID();
        String cmd = String.format(CMD, downloadUrl, savesPath);
        ProcessResult pr = MutiProcessUtil.runShellProcess(uuid, cmd);
        return pr.isSuccess();
    }

    /**
     * @see ObjectDownloadComponent#getCloud()
     */
    @Override
    public String getCloud() {
        return "curl";
    }
}
