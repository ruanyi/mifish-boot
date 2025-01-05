package com.ruanyi.mifish.image.trigger;

import org.springframework.stereotype.Component;

import com.ruanyi.mifish.kaproxy.annotation.KaproxyConsumer;
import com.ruanyi.mifish.kaproxy.model.MessageStatus;
import com.ruanyi.mifish.kernel.model.msg.StdMessage;

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
    @KaproxyConsumer(group = "mifish_image_group", topics = {"img_0", "img_1", "img_2"})
    public MessageStatus doConsume(StdMessage stdMessage) {
        return MessageStatus.SUCCESS;
    }
}
