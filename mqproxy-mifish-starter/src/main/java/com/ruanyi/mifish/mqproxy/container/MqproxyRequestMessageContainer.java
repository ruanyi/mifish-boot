package com.ruanyi.mifish.mqproxy.container;

import java.util.ArrayList;
import java.util.List;

import com.ruanyi.mifish.common.utils.RandomUtil;
import com.ruanyi.mifish.mqproxy.model.MqproxyRequestMessage;

/**
 * Description:
 * 
 * 必须是个单例
 *
 * @author: ruanyi
 * @Date: 2024-05-16 22:47
 */
public final class MqproxyRequestMessageContainer {

    /**
     * 考虑到：优先级消费，里面的对象可重复，因此，不能使用：Set<br>
     * 系统启动时，就已设置好，支持：<br>
     * 1、随机消费<br>
     * 2、顺序搜索
     */
    private List<MqproxyRequestMessage> mqproxyRequestMessages = new ArrayList<>();

    /**
     * randomSelectOne
     * 
     * @return
     */
    public MqproxyRequestMessage randomSelectOne() {
        return RandomUtil.randomSelectOne(this.mqproxyRequestMessages);
    }

    /**
     * selectOne
     * 
     * @param index
     * @return
     */
    public MqproxyRequestMessage selectOne(int index) {
        if (index < this.mqproxyRequestMessages.size()) {
            return this.mqproxyRequestMessages.get(index);
        }
        return null;
    }

    /**
     * 不允许多线程并发 add
     *
     * @param mqproxyRequestMessage
     * @return
     */
    public synchronized MqproxyRequestMessageContainer
        addMqproxyRequestMessage(MqproxyRequestMessage mqproxyRequestMessage) {
        if (mqproxyRequestMessage != null) {
            this.mqproxyRequestMessages.add(mqproxyRequestMessage);
        }
        return this;
    }

    /**
     * 批量新增多个一模一样的mqproxyRequestMessage
     *
     * @param mqproxyRequestMessage
     * @param weight
     * @return
     */
    public synchronized MqproxyRequestMessageContainer
        addMqproxyRequestMessages(MqproxyRequestMessage mqproxyRequestMessage, int weight) {
        if (weight > 0 && mqproxyRequestMessage != null) {
            for (int i = 0; i < weight; i++) {
                this.mqproxyRequestMessages.add(mqproxyRequestMessage);
            }
        }
        return this;
    }

    private MqproxyRequestMessageContainer() {

    }

    /**
     * getInstance
     *
     * @return
     */
    public static MqproxyRequestMessageContainer getInstance() {
        return MqproxyRequestMessageContainerHolder.INSTANCE;
    }

    /**
     * Description:
     *
     * @author: ruanyi
     * @Date: 2018-09-04 19:46
     */
    private static class MqproxyRequestMessageContainerHolder {

        private static MqproxyRequestMessageContainer INSTANCE = new MqproxyRequestMessageContainer();

        private MqproxyRequestMessageContainerHolder() {

        }
    }
}
