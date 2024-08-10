package com.ruanyi.mifish.download.impl;

import java.util.HashMap;
import java.util.Map;

import com.ruanyi.mifish.download.ObjectDownloadComponent;
import com.ruanyi.mifish.download.ObjectDownloader;
import com.ruanyi.mifish.kernel.model.uri.DownloadURI;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-23 21:32
 */
public class SimpleObjectDownloadComponent implements ObjectDownloadComponent {

    /** s3ObjectDownloader */
    private ObjectDownloader s3ObjectDownloader = null;

    /** downloaders */
    private Map<String, ObjectDownloader> downloaders = new HashMap<>(12);

    /**
     * @see ObjectDownloadComponent#download(DownloadURI, String)
     */
    @Override
    public boolean download(DownloadURI downloadURI, String savesPath) {
        // 1、如果是当前小文件存储系统，支持s3协议
        if (downloadURI.isSupportS3()) {
            this.s3ObjectDownloader.download(downloadURI, savesPath);
        }
        // 1、根据url解析出：cloud
        // 2、根据解析出的cloud，路由选择具体的下载组件，触发下载
        if (this.downloaders.containsKey(downloadURI.getCloud())) {
            ObjectDownloader downloader = this.downloaders.get(downloadURI.getCloud());
            // todo 重试多次，
            downloader.download(downloadURI, savesPath);
        }
        // 3、如果无法解析出来，则通过curl * -o savesPath来处理
        ObjectDownloader curlDownloader = this.downloaders.get("curl");
        boolean isSuccess = curlDownloader.download(downloadURI, savesPath);
        return isSuccess;
    }
}
