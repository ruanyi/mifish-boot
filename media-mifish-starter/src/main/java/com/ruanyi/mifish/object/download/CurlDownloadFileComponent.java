package com.ruanyi.mifish.object.download;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-09-02 18:12
 */
public interface CurlDownloadFileComponent {

    /**
     * download
     *
     * @param url
     * @param outputDir
     * @return
     */
    boolean download(String url, String outputDir);
}
