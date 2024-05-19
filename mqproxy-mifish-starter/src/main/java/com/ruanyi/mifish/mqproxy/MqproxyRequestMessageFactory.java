package com.ruanyi.mifish.mqproxy;

import com.ruanyi.mifish.mqproxy.container.MqproxyRequestMessageContainer;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-19 14:50
 */
public interface MqproxyRequestMessageFactory {

    /**
     * batchBuild
     *
     * @param effectGroupTopics
     * @return
     */
    MqproxyRequestMessageContainer batchBuild(String effectGroupTopics);
}
