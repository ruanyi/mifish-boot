package com.ruanyi.mifish.object.download;

import org.junit.Assert;
import org.junit.Test;

import com.ruanyi.mifish.env.LocalTmpWorkEnv;
import com.ruanyi.mifish.model.TmpWorkEnv;
import com.ruanyi.mifish.object.download.impl.CurlDownloadFileComponentImpl;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-02 19:02
 */
public class CurlDownloadFileComponentTest {

    private static CurlDownloadFileComponentImpl curlDownloadFileComponent = new CurlDownloadFileComponentImpl();

    @Test
    public void test1() {
        String videoUrl = "http://vds.data.com/l_video/64f30b727a5808pea8zm8e3252.mov";
        TmpWorkEnv tmpWorkEnv = LocalTmpWorkEnv.prepareTmpEnv();
        boolean isSuccess = curlDownloadFileComponent.download(videoUrl, tmpWorkEnv.getWorkDir());
        System.out.println(isSuccess);
        Assert.assertFalse(isSuccess);
    }
}
