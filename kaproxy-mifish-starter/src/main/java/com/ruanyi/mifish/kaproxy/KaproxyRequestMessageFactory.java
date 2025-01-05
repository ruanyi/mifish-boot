package com.ruanyi.mifish.kaproxy;

import com.ruanyi.mifish.kaproxy.container.KaproxyRequestMessageContainer;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-19 14:50
 */
public interface KaproxyRequestMessageFactory {

    /**
     * batchBuild
     *
     * @param effectGroupTopics
     * @return
     */
    KaproxyRequestMessageContainer batchBuild(String effectGroupTopics);
}
