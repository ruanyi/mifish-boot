package com.ruanyi.mifish.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruanyi.mifish.common.model.OperateResult;
import com.ruanyi.mifish.web.service.ImageResizeService;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-08-31 18:31
 */
@RestController
@RequestMapping("/api/v1/image")
public class ImageController {

    /** imageResizeService */
    @Autowired
    private ImageResizeService imageResizeService;

    /**
     * batchResize
     *
     * @return
     */
    @PostMapping(value = "/resize")
    public OperateResult batchResize() {
        String fromImageDir = "/Users/rls/Documents/tmp/v19/封面图";
        String toImageDir = "/Users/rls/Documents/tmp/v19/img_rs";
        this.imageResizeService.asyncResize(fromImageDir, toImageDir);
        return OperateResult.SUCCESS(true);
    }
}
