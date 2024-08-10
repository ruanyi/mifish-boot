package com.ruanyi.mifish.download;

import com.ruanyi.mifish.common.ex.BusinessException;
import com.ruanyi.mifish.kernel.model.uri.DownloadURI;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-04-28 23:50
 */
public interface ObjectDownloader {

    /**
     * download
     * 
     * @param downloadURI
     * @param savesPath
     * @return
     * @throws BusinessException
     */
    boolean download(DownloadURI downloadURI, String savesPath) throws BusinessException;

    /**
     * getCloud
     * 
     * @return
     */
    String getCloud();
}
