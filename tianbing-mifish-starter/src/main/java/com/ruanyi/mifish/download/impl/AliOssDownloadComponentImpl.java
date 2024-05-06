package com.ruanyi.mifish.download.impl;

import com.ruanyi.mifish.common.ex.BusinessException;
import com.ruanyi.mifish.download.ObjectDownloadComponent;
import com.ruanyi.mifish.kernel.model.uri.ObjectURI;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-04 10:27
 */
public class AliOssDownloadComponentImpl implements ObjectDownloadComponent {
    /**
     * @see ObjectDownloadComponent#download(ObjectURI, String)
     */
    @Override
    public boolean download(ObjectURI objectURI, String savesPath) throws BusinessException {
        return false;
    }

    /**
     * @see ObjectDownloadComponent#getCloud()
     */
    @Override
    public String getCloud() {
        return "aliyun_oss";
    }
}
