package com.ruanyi.mifish.upload;

import com.ruanyi.mifish.common.ex.BusinessException;
import com.ruanyi.mifish.kernel.model.msg.SaveasURI;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-04 10:21
 */
public interface ObjectUploader {

    /**
     * upload
     * 
     * @param localPath
     * @param saveasURI
     * @return
     * @throws BusinessException
     */
    boolean upload(String localPath, SaveasURI saveasURI) throws BusinessException;
}
