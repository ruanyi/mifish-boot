package com.ruanyi.mifish.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruanyi.mifish.common.model.OperateResult;
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
    @Autowired
    private ImageResizeService imageResizeService;

    /** imageCompressService */
    @Autowired
    private ImageCompressService imageCompressService;

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
