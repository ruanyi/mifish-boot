package com.ruanyi.mifish.video.trigger;

import org.springframework.stereotype.Component;

import com.ruanyi.mifish.kaproxy.annotation.KaproxyConsumer;
import com.ruanyi.mifish.kaproxy.model.MessageStatus;
import com.ruanyi.mifish.kernel.model.msg.StdMessage;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-04-21 18:00
 */

@Component
public class VideoStdTrigger {

    /**
     * doConsume
     *
     * @param stdMessage
     */
    @KaproxyConsumer(group = "mifish_video_group", topicPattern = "mifish.video.topics")
    public MessageStatus doConsume(StdMessage stdMessage) {

        return MessageStatus.SUCCESS;
    }
}
