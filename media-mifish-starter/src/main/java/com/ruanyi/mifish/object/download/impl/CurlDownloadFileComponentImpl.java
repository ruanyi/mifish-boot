package com.ruanyi.mifish.object.download.impl;

import org.springframework.stereotype.Component;

import com.ruanyi.mifish.common.model.ProcessResult;
import com.ruanyi.mifish.common.utils.MutiProcessUtil;
import com.ruanyi.mifish.common.utils.UUIDUtil;
import com.ruanyi.mifish.object.download.CurlDownloadFileComponent;
import com.ruanyi.mifish.object.url.ObjectUrlHelper;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-09-02 18:13
 */
@Component
public class CurlDownloadFileComponentImpl implements CurlDownloadFileComponent {

    /** CMD */
    private static final String CMD = "curl \"%s\" -o %s";

    /**
     * @see CurlDownloadFileComponent#download(String, String)
     */
    @Override
    public boolean download(String url, String outputDir) {
        String uuid = UUIDUtil.obtainUUID();
        String lastKey = ObjectUrlHelper.parseLastKey(url);
        String savesPath = outputDir + "/" + lastKey;
        String cmd = String.format(CMD, url, savesPath);
        ProcessResult pr = MutiProcessUtil.runShellProcess(uuid, cmd);
        return pr.isSuccess();
    }
}
