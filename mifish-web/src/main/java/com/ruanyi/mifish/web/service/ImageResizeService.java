package com.ruanyi.mifish.web.service;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-08-31 20:12
 */
public interface ImageResizeService {

    /**
     * asyncResize
     *
     * @param fromImageDir
     * @param toImageDir
     */
    void asyncResize(String fromImageDir, String toImageDir);
}
