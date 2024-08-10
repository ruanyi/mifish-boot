package com.ruanyi.mifish.download.impl;

import com.ruanyi.mifish.common.ex.BusinessException;
import com.ruanyi.mifish.download.ObjectDownloader;
import com.ruanyi.mifish.kernel.model.uri.DownloadURI;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-04 10:27
 */
public class AliOssObjectDownloader implements ObjectDownloader {
    /**
     * @see ObjectDownloader#download(DownloadURI, String)
     */
    @Override
    public boolean download(DownloadURI downloadURI, String savesPath) throws BusinessException {
        return false;
    }

    /**
     * @see ObjectDownloader#getCloud()
     */
    @Override
    public String getCloud() {
        return "aliyun_oss";
    }
}
