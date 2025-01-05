package com.ruanyi.mifish.upload.impl;

import com.ruanyi.mifish.common.ex.BusinessException;
import com.ruanyi.mifish.kernel.model.uri.UploadURI;
import com.ruanyi.mifish.upload.ObjectUploader;

/**
 * Description:
 *
 * @author rls
 * @date 2024-10-13 10:02
 */
public class AliOssObjectUploader implements ObjectUploader {

    /**
     * @see ObjectUploader#upload(String, UploadURI)
     */
    @Override
    public boolean upload(String localPath, UploadURI uploadURI) throws BusinessException {
        return false;
    }
}
