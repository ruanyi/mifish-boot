package com.mifish.common.event;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2018-04-15 10:09
 */
public abstract class MifishEvent implements Serializable {

    private static final long serialVersionUID = -5141361800580540937L;

    /**
     * eventId
     */
    private String eventId;


    /**
     * MifishEvent
     */
    public MifishEvent() {
        this(UUID.randomUUID().toString());
    }

    /**
     * MifishEvent
     *
     * @param eventId
     */
    public MifishEvent(String eventId) {
        this.eventId = eventId;
    }

    /**
     * getEventId
     *
     * @return
     */
    public String getEventId() {
        return eventId;
    }
}
