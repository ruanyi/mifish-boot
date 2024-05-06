package com.ruanyi.mifish.download;

import org.junit.Assert;
import org.junit.Test;

import com.ruanyi.mifish.common.utils.UUIDUtil;
import com.ruanyi.mifish.download.impl.CurlDownloadComponentImpl;
import com.ruanyi.mifish.env.LocalTmpWorkEnv;
import com.ruanyi.mifish.kernel.model.uri.ObjectURI;
import com.ruanyi.mifish.model.env.TmpWorkEnv;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-09-02 19:02
 */
public class CurlDownloadComponentImplTest {

    private static CurlDownloadComponentImpl CURL_DOWNLOADER = new CurlDownloadComponentImpl();

    @Test
    public void test1() {
        String videoUrl = "http://vds.data.com/l_video/64f30b727a5808pea8zm8e3252.mov";
        ObjectURI objectURI = new ObjectURI();
        objectURI.setUrl(videoUrl);
        TmpWorkEnv tmpWorkEnv = LocalTmpWorkEnv.prepareTmpEnv();
        String savesPath = tmpWorkEnv + "/" + UUIDUtil.obtainUUID() + ".mp4";
        boolean isSuccess = CURL_DOWNLOADER.download(objectURI, savesPath);
        System.out.println(isSuccess);
        Assert.assertFalse(isSuccess);
    }
}
