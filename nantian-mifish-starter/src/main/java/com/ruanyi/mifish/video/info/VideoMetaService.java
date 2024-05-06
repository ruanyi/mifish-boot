package com.ruanyi.mifish.video.info;

import com.ruanyi.mifish.kernel.model.info.AvInfo;

/**
 * Description:
 *
 * @author: ruanyi
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
