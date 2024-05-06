package com.ruanyi.mifish.model.env;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-09-02 18:51
 */
public class TmpWorkEnv {

    /** workDir */
    private final String workDir;

    /** uuid */
    private final String uuid;

    /**
     * TmpWorkEnv
     *
     * @param workDir
     * @param uuid
     */
    public TmpWorkEnv(String workDir, String uuid) {
        this.workDir = workDir;
        this.uuid = uuid;
    }

    /**
     * getWorkDir
     *
     * @return
     */
    public String getWorkDir() {
        return workDir;
    }

    /**
     * getUuid
     * 
     * @return
     */
    public String getUuid() {
        return uuid;
    }
}
