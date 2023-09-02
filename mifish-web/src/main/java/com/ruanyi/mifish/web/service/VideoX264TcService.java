package com.ruanyi.mifish.web.service;

/**
 * Description:
 *
 * @author: ruanyi
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

    /**
     * syncX264Tc
     * 
     * @param videoUrl
     * @return
     */
    String syncX264Tc(String videoUrl);
}
