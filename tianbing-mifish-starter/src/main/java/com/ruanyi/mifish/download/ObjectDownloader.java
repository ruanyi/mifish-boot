package com.ruanyi.mifish.download;

import com.ruanyi.mifish.kernel.model.uri.ObjectURI;

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
     * @param objectURI
     * @param savesPath
     * @return
     */
    boolean download(ObjectURI objectURI, String savesPath);
}
