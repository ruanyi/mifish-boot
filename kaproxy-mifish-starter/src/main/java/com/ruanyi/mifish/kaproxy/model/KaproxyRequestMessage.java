package com.ruanyi.mifish.kaproxy.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-11-18 13:44
 */
@Getter
@Setter
public class KaproxyRequestMessage {

    /** url */
    private String url;

    /** group */
    private String group;

    /** topic */
    private String topic;

    /** poolName */
    private String poolName;

    /** host */
    private String host;

    /** token */
    private String token;
}
