package com.ruanyi.mifish.web.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;

import com.ruanyi.mifish.common.model.MifishResponse;
import com.ruanyi.mifish.common.model.OperateResult;
import com.ruanyi.mifish.kernel.check.MifishCheck;
import com.ruanyi.mifish.model.media.MediaAsyncRequest;
import com.ruanyi.mifish.web.service.ImageCompressService;
import com.ruanyi.mifish.web.service.ImageResizeService;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-08-31 18:31
 */
@RestController
@RequestMapping("/api/v1/image")
public class ImageController {

    /** imageResizeService */
    @Resource
    private ImageResizeService imageResizeService;

    /** imageCompressService */
    @Resource
    private ImageCompressService imageCompressService;

    /**
     * asyncInvoke
     *
     * @param fnCode
     * @param mediaAsyncRequest
     * @return
     */
    @MifishCheck(isSign = true, isAuth = true)
    @PostMapping(value = "/async/{fn_code}")
    public MifishResponse asyncInvoke(@PathVariable("fn_code") String fnCode,
        @RequestBody MediaAsyncRequest mediaAsyncRequest) {
        String msgId = null;
        return MifishResponse.SUCCESS(msgId);
    }

    /**
     * batchResize
     *
     * @return
     */
    @PostMapping(value = "/resize")
    public OperateResult batchResize() {
        String fromImageDir = "/Users/ruanyi/Documents/tmp/v19/封面图";
        String toImageDir = "/Users/ruanyi/Documents/tmp/v19/img_rs";
        this.imageResizeService.asyncResize(fromImageDir, toImageDir);
        return OperateResult.SUCCESS(true);
    }

    /**
     * compress
     *
     * @return
     */
    @PostMapping(value = "/compress")
    public OperateResult batchCompress() {
        String fromImageDir = "/Users/ruanyi/Documents/tmp/v19/封面图";
        String toImageDir = "/Users/ruanyi/Documents/tmp/v19/img_rs";
        this.imageCompressService.asyncCompress(fromImageDir, toImageDir);
        return OperateResult.SUCCESS(true);
    }
}
