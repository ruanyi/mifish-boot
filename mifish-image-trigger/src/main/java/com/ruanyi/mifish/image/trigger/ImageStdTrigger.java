package com.ruanyi.mifish.image.trigger;

import org.springframework.stereotype.Component;

import com.ruanyi.mifish.kernel.model.msg.StdMessage;
import com.ruanyi.mifish.mqproxy.annotation.MqproxyConsumer;
import com.ruanyi.mifish.mqproxy.model.MessageStatus;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-04-21 18:01
 */
@Component
public class ImageStdTrigger {

    /**
     * doConsume
     * 
     * @param stdMessage
     */
    @MqproxyConsumer(group = "mifish_image_group", topics = {"img_0", "img_1", "img_2"})
    public MessageStatus doConsume(StdMessage stdMessage) {
        return MessageStatus.SUCCESS;
    }
}
