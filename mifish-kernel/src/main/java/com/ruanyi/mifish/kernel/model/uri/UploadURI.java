package com.ruanyi.mifish.kernel.model.uri;

import com.ruanyi.mifish.kernel.model.storage.Credential;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-31 21:03
 */
@Getter
@Setter
public class UploadURI {

    /** cloud */
    private String cloud;

    /** supportS3 */
    private boolean supportS3;

    /** bucket */
    private String bucket;

    /** key */
    private String key;

    /** credential */
    private Credential credential;
}
