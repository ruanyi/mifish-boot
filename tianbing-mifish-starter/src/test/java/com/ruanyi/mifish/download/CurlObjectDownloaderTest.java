package com.ruanyi.mifish.download;

import org.junit.Assert;
import org.junit.Test;

import com.ruanyi.mifish.common.utils.UUIDUtil;
import com.ruanyi.mifish.download.impl.CurlObjectDownloader;
import com.ruanyi.mifish.env.LocalTmpWorkEnv;
import com.ruanyi.mifish.kernel.model.uri.DownloadURI;
import com.ruanyi.mifish.model.env.TmpWorkEnv;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-09-02 19:02
 */
public class CurlObjectDownloaderTest {

    private static CurlObjectDownloader CURL_DOWNLOADER = new CurlObjectDownloader();

    @Test
    public void test1() {
        String videoUrl = "http://vds.data.com/l_video/64f30b727a5808pea8zm8e3252.mov";
        DownloadURI downloadURI = new DownloadURI();
        downloadURI.setUrl(videoUrl);
        TmpWorkEnv tmpWorkEnv = LocalTmpWorkEnv.prepareTmpEnv();
        String savesPath = tmpWorkEnv + "/" + UUIDUtil.obtainUUID() + ".mp4";
        boolean isSuccess = CURL_DOWNLOADER.download(downloadURI, savesPath);
        System.out.println(isSuccess);
        Assert.assertFalse(isSuccess);
    }
}
