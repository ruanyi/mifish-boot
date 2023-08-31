package com.ruanyi.mifish.image;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-08-31 17:45
 */
public interface ImageMagicResizeService {

    /**
     * resize1080
     * 
     * @param fromImagePath
     * @param toImagePath
     * @return
     */
    boolean resize1080(String fromImagePath, String toImagePath);
}
