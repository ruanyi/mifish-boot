package com.ruanyi.mifish.kernel.model.msg;

import java.util.Map;

import com.ruanyi.mifish.common.annotation.OpenApi;
import com.ruanyi.mifish.kernel.model.storage.Credential;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-04-21 23:45
 */
@Getter
@Setter
@OpenApi(scene = "输出文件")
public class OutputFile {

    /** cmd */
    private String cmd;

    /** code */
    private String code;

    /** message */
    private String message;

    /** cloud */
    private String cloud;

    /** bucket */
    private String bucket;

    /** key */
    private String key;

    /** credential */
    private Credential credential;

    /** extra */
    private Map<String, Object> extra;
}
