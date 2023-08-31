package com.ruanyi.mifish.video.info;

import com.ruanyi.mifish.model.AvInfo;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-08-31 13:51
 */
public interface VideoMetaService {

    /**
     * obtainVideoMetaByFFprobe
     * 
     * @param videoPath
     * @return
     */
    AvInfo obtainVideoMetaByFFprobe(String videoPath);
}
