package com.ruanyi.mifish.download.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ruanyi.mifish.download.ObjectDownloadComponent;
import com.ruanyi.mifish.download.ObjectDownloader;
import com.ruanyi.mifish.kernel.model.uri.ObjectURI;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-03 21:29
 */
@Component
public class SimpleObjectDownloader implements ObjectDownloader {

    /** downloadComponents */
    private Map<String, ObjectDownloadComponent> downloadComponents = new HashMap<>(12);

    /**
     * @see ObjectDownloader#download(ObjectURI, String)
     */
    @Override
    public boolean download(ObjectURI objectURI, String savesPath) {
        // 1、根据url解析出：cloud
        // 2、根据解析出的cloud，路由选择具体的下载组件，触发下载
        if (this.downloadComponents.containsKey(objectURI.getCloud())) {
            ObjectDownloadComponent downloadComponent = this.downloadComponents.get(objectURI.getCloud());
            // todo 重试多次，
            downloadComponent.download(objectURI, savesPath);
        }
        // 3、如果无法解析出来，则通过curl * -o savesPath来处理
        ObjectDownloadComponent curlDownloadComponent = this.downloadComponents.get("curl");
        boolean isSuccess = curlDownloadComponent.download(objectURI, savesPath);
        return isSuccess;
    }
}
