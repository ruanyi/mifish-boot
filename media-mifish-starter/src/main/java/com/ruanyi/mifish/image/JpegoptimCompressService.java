package com.ruanyi.mifish.image;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-01 14:28
 */
public interface JpegoptimCompressService {

    /**
     * compress
     * 
     * @param quality
     * @param fromImagePath
     * @param outDir
     * @return
     */
    boolean compress(int quality, String fromImagePath, String outDir);
}
