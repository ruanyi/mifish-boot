package com.ruanyi.mifish.video.trigger;

import org.springframework.stereotype.Component;

import com.ruanyi.mifish.kernel.model.msg.StdMessage;
import com.ruanyi.mifish.mqproxy.annotation.MqproxyConsumer;
import com.ruanyi.mifish.mqproxy.model.MessageStatus;

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
    @MqproxyConsumer(group = "mifish_video_group", topicPattern = "mifish.video.topics")
    public MessageStatus doConsume(StdMessage stdMessage) {

        return MessageStatus.SUCCESS;
    }
}
