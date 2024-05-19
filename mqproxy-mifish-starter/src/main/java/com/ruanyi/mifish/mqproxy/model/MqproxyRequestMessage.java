package com.ruanyi.mifish.mqproxy.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 * 
 * 一旦创建好后，不允许修改
 *
 * @author: ruanyi
 * @Date: 2022-11-18 13:44
 */
@Getter
@Setter
public class MqproxyRequestMessage {

    /** groupTopic */
    private GroupTopic groupTopic;

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
