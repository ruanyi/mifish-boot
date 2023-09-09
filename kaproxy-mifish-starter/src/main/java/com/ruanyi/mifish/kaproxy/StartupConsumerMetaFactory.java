package com.ruanyi.mifish.kaproxy;

import java.util.List;
import java.util.Set;

import com.ruanyi.mifish.kaproxy.model.ConsumerStartupStrategy;
import com.ruanyi.mifish.kaproxy.model.GroupTopic;
import com.ruanyi.mifish.kaproxy.model.StartupConsumerMeta;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2021-11-06 11:19
 */
public interface StartupConsumerMetaFactory {

    /**
     * batchBuild
     * 
     * @param groupTopics
     * @param strategy
     * @return
     * @throws Exception
     */
    List<StartupConsumerMeta> batchBuild(Set<GroupTopic> groupTopics, ConsumerStartupStrategy strategy)
        throws Exception;
}
