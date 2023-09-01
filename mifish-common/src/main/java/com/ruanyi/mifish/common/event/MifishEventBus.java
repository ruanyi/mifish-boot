package com.ruanyi.mifish.common.event;

import com.google.common.eventbus.EventBus;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2018-04-15 10:06
 */
public class MifishEventBus {

    /*** EVENT_BUS */
    private static final EventBus EVENT_BUS = new EventBus("mifish");

    /**
     * 触发同步事件
     *
     * @param event
     */
    public static void post(Object event) {
        EVENT_BUS.post(event);
    }

    /**
     * 注册事件处理器
     *
     * @param handler
     */
    public static void register(Object handler) {
        EVENT_BUS.register(handler);
    }

    /**
     * 注销事件处理器
     *
     * @param handler
     */
    public static void unregister(Object handler) {
        EVENT_BUS.unregister(handler);
    }

    /** forbit instance */
    private MifishEventBus() {

    }
}
