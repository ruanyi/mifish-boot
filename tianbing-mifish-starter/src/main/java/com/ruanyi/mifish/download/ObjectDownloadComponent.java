package com.ruanyi.mifish.download;

import com.ruanyi.mifish.common.ex.BusinessException;
import com.ruanyi.mifish.kernel.model.uri.ObjectURI;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-03 21:30
 */
public interface ObjectDownloadComponent {

    /**
     * download
     * 
     * @param objectURI
     * @param savesPath
     * @return
     * @throws BusinessException
     */
    boolean download(ObjectURI objectURI, String savesPath) throws BusinessException;

    /**
     * getCloud
     * 
     * @return
     */
    String getCloud();

}
