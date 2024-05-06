package com.ruanyi.mifish.kernel.model.msg;

import com.ruanyi.mifish.common.annotation.OpenApi;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-04-21 23:33
 */
@Getter
@Setter
@OpenApi(scene = "另存为URI")
public class SaveasURI {

    /** cloud */
    private String cloud;

    /** bucket */
    private String bucket;

    /** key */
    private String key;

}
