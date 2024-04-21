package com.ruanyi.mifish.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ruanyi.mifish.common.model.MifishResponse;
import com.ruanyi.mifish.common.model.OperateResult;
import com.ruanyi.mifish.kernel.check.MifishCheck;
import com.ruanyi.mifish.media.MediaAsyncRequest;
import com.ruanyi.mifish.web.service.VideoX264TcService;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-08-31 18:30
 */
@RestController
@RequestMapping("/api/v1/video")
public class VideoController {

    /** videoX264TcService */
    @Autowired
    private VideoX264TcService videoX264TcService;

    /**
     * asyncInvoke
     * 
     * @param fnCode
     * @param mediaAsyncRequest
     * @return
     */
    @MifishCheck(isSignCheck = true)
    @PostMapping(value = "/async/{fn_code}")
    public MifishResponse asyncInvoke(@PathVariable("fn_code") String fnCode,
        @RequestBody MediaAsyncRequest mediaAsyncRequest) {
        String msgId = null;
        return MifishResponse.SUCCESS(msgId);
    }

    /**
     * batchX264CrfTc
     * 
     * @return
     */
    @PostMapping(value = "/x264/crf/tc/batch")
    public OperateResult batchX264CrfTc() {
        String fromVideoDir = "/Users/ruanyi/Documents/tmp/v19/素材";
        String toVideoDir = "/Users/ruanyi/Documents/tmp/v19/video_rs";
        this.videoX264TcService.asyncX264Tc(fromVideoDir, toVideoDir);
        return OperateResult.SUCCESS(true);
    }

    /**
     * batchCheckFrameNums
     *
     * @return
     */
    @PostMapping(value = "/check/frame/nums/batch")
    public OperateResult batchCheckFrameNums() {
        String fromVideoDir = "/Users/rls/Documents/tmp/v38";
        this.videoX264TcService.asyncCheckFrameNums(fromVideoDir);
        return OperateResult.SUCCESS(true);
    }

    /**
     * singleX264CrfTc
     *
     * @return
     */
    @PostMapping(value = "/x264/crf/tc")
    public OperateResult singleX264CrfTc(@RequestParam("video_url") String videoUrl) {
        String outputUrl = this.videoX264TcService.syncX264Tc(videoUrl);
        return OperateResult.SUCCESS(outputUrl);
    }
}
