package com.ruanyi.mifish.kernel.model.uri;

import com.ruanyi.mifish.kernel.model.storage.Credential;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-30 23:16
 */
@Getter
@Setter
public class DownloadURI {

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

    /** credential */
    private Credential credential;
}
