package com.ruanyi.mifish.upload.impl;

import org.springframework.stereotype.Component;

import com.ruanyi.mifish.common.ex.BusinessException;
import com.ruanyi.mifish.kernel.model.msg.SaveasURI;
import com.ruanyi.mifish.upload.ObjectUploader;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-04 10:29
 */
@Component
public class SimpleObjectUploader implements ObjectUploader {

    /**
     * @see ObjectUploader#upload(String, SaveasURI)
     */
    @Override
    public boolean upload(String localPath, SaveasURI saveasURI) throws BusinessException {
        return false;
    }
}
