package com.ruanyi.mifish.kernel.model.uri;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-04-28 23:50
 */
@Getter
@Setter
public class ObjectURI {

    /** cloud */
    private String cloud;

    /** supportS3 */
    private boolean supportS3;

    /** bucket */
    private String bucket;

    /** key */
    private String key;

    /** url */
    private String url;

}
