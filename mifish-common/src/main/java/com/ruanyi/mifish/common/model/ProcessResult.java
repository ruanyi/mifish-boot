package com.ruanyi.mifish.common.model;

import java.util.Arrays;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-08-31 13:59
 */
@Getter
@Setter
public class ProcessResult {

    /** exitValue */
    private int exitValue;

    /** pid */
    private String pid;

    /** cmdStr */
    private String cmdStr;

    /** stdout */
    private String stdout;

    /** stderr */
    private String stderr;

    /**
     * isSuccess
     * 
     * @return
     */
    public boolean isSuccess() {
        return this.exitValue == 0;
    }

    /**
     * ALREADY_EXISTS
     * 
     * @param pid
     * @param cmd
     * @return
     */
    public static ProcessResult ALREADY_EXISTS(String pid, String... cmd) {
        return FAIL(pid, -1, cmd);
    }

    /**
     * FAIL
     *
     * @param pid
     * @param exitValue
     * @param cmd
     * @return
     */
    public static ProcessResult FAIL(String pid, int exitValue, String... cmd) {
        ProcessResult processResult = new ProcessResult();
        processResult.pid = pid;
        processResult.exitValue = exitValue;
        processResult.cmdStr = Arrays.toString(cmd);
        return processResult;
    }
}
