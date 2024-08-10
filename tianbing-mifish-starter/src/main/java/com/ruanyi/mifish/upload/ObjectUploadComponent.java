package com.ruanyi.mifish.upload;

import com.ruanyi.mifish.common.ex.BusinessException;
import com.ruanyi.mifish.kernel.model.uri.UploadURI;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-23 22:49
 */
public interface ObjectUploadComponent {

    /**
     * upload
     *
     * @param localPath
     * @param uploadURI
     * @return
     * @throws BusinessException
     */
    boolean upload(String localPath, UploadURI uploadURI) throws BusinessException;
}
