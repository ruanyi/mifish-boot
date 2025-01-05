package com.ruanyi.mifish.upload;

import com.ruanyi.mifish.common.ex.BusinessException;
import com.ruanyi.mifish.kernel.model.uri.UploadURI;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-04 10:21
 */
public interface ObjectUploader {

    /**
     * 如果OSS文件存在，则上传的数据会覆盖该文件的内容；如果OSS文件不存在，则会新建该文件。
     *
     * @param localPath
     * @param uploadURI
     * @return
     * @throws BusinessException
     */
    boolean upload(String localPath, UploadURI uploadURI) throws BusinessException;
}
