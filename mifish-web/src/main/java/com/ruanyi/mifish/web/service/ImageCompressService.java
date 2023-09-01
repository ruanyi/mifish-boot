package com.ruanyi.mifish.web.service;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-01 14:33
 */
public interface ImageCompressService {

    /**
     * asyncCompress
     *
     * @param fromImageDir
     * @param toImageDir
     */
    void asyncCompress(String fromImageDir, String toImageDir);
}
