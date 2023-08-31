package com.ruanyi.mifish.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruanyi.mifish.common.model.OperateResult;
import com.ruanyi.mifish.web.service.VideoX264TcService;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-08-31 18:30
 */
@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    /** videoX264TcService */
    @Autowired
    private VideoX264TcService videoX264TcService;

    /**
     * batchX264CrfTc
     * 
     * @return
     */
    @PostMapping(value = "/x264/crf/tc")
    public OperateResult batchX264CrfTc() {
        String fromVideoDir = "/Users/rls/Documents/tmp/v19/素材";
        String toVideoDir = "/Users/rls/Documents/tmp/v19/video_rs";
        this.videoX264TcService.asyncX264Tc(fromVideoDir, toVideoDir);
        return OperateResult.SUCCESS(true);
    }
}
