package com.ruanyi.mifish.env;

import java.io.File;

import com.ruanyi.mifish.common.utils.UUIDUtil;
import com.ruanyi.mifish.model.env.TmpWorkEnv;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-09-02 18:42
 */
public final class LocalTmpWorkEnv {

    /**
     * prepareTmpEnv
     * 
     * @return
     */
    public static TmpWorkEnv prepareTmpEnv() {
        String uuid = UUIDUtil.obtainUUID();
        String tmpDir = "/tmp/mifish/" + uuid;
        File file = new File(tmpDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return new TmpWorkEnv(tmpDir, uuid);
    }

    /**
     * cleanTmpEnv
     * 
     * @param tmpWorkEnv
     * @return
     */
    public static boolean cleanTmpEnv(TmpWorkEnv tmpWorkEnv) {
        File file = new File(tmpWorkEnv.getWorkDir());
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

    /** LocalTmpWorkEnv */
    private LocalTmpWorkEnv() {

    }
}
