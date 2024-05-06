package com.ruanyi.mifish.audio;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-08-31 11:38
 */
public interface AudioExtractService {

    /**
     * extract
     *
     * @param fromVideoPath
     * @param saveasPath
     * @return
     */
    boolean extract(String fromVideoPath, String saveasPath);
}
