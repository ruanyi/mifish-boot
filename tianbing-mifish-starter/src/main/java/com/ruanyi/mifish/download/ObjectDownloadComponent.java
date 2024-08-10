package com.ruanyi.mifish.download;

import com.ruanyi.mifish.kernel.model.uri.DownloadURI;

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
     * @param downloadURI
     * @param savesPath
     * @return
     */
    boolean download(DownloadURI downloadURI, String savesPath);
}
