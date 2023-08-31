package com.ruanyi.mifish.web.service;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-08-31 18:34
 */
public interface VideoX264TcService {

    /**
     * asyncX264Tc
     *
     * @param fromVideoDir
     * @param toVideoDir
     */
    void asyncX264Tc(String fromVideoDir, String toVideoDir);
}
