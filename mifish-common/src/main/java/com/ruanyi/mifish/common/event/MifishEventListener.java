package com.ruanyi.mifish.common.event;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2018-04-15 10:07
 */
public class MifishEventListener {

    /**
     * init
     *
     * @throws Exception
     */
    public void init() throws Exception {
        MifishEventBus.register(this);
    }

    /**
     * destroy
     *
     * @throws Exception
     */
    public void destroy() throws Exception {
        MifishEventBus.unregister(this);
    }
}
