package com.ruanyi.mifish.kaproxy.container;

import java.util.ArrayList;
import java.util.List;

import com.ruanyi.mifish.common.utils.RandomUtil;
import com.ruanyi.mifish.kaproxy.model.KaproxyRequestMessage;

/**
 * Description:
 * 
 * 必须是个单例
 *
 * @author: ruanyi
 * @Date: 2024-05-16 22:47
 */
public final class KaproxyRequestMessageContainer {

    /**
     * 考虑到：优先级消费，里面的对象可重复，因此，不能使用：Set<br>
     * 系统启动时，就已设置好，支持：<br>
     * 1、随机消费<br>
     * 2、顺序搜索
     */
    private List<KaproxyRequestMessage> kaproxyRequestMessages = new ArrayList<>();

    /**
     * randomSelectOne
     * 
     * @return
     */
    public KaproxyRequestMessage randomSelectOne() {
        return RandomUtil.randomSelectOne(this.kaproxyRequestMessages);
    }

    /**
     * selectOne
     * 
     * @param index
     * @return
     */
    public KaproxyRequestMessage selectOne(int index) {
        if (index < this.kaproxyRequestMessages.size()) {
            return this.kaproxyRequestMessages.get(index);
        }
        return null;
    }

    /**
     * 不允许多线程并发 add
     *
     * @param kaproxyRequestMessage
     * @return
     */
    public synchronized KaproxyRequestMessageContainer
        addMqproxyRequestMessage(KaproxyRequestMessage kaproxyRequestMessage) {
        if (kaproxyRequestMessage != null) {
            this.kaproxyRequestMessages.add(kaproxyRequestMessage);
        }
        return this;
    }

    /**
     * 批量新增多个一模一样的mqproxyRequestMessage
     *
     * @param kaproxyRequestMessage
     * @param weight
     * @return
     */
    public synchronized KaproxyRequestMessageContainer
        addMqproxyRequestMessages(KaproxyRequestMessage kaproxyRequestMessage, int weight) {
        if (weight > 0 && kaproxyRequestMessage != null) {
            for (int i = 0; i < weight; i++) {
                this.kaproxyRequestMessages.add(kaproxyRequestMessage);
            }
        }
        return this;
    }

    private KaproxyRequestMessageContainer() {

    }

    /**
     * getInstance
     *
     * @return
     */
    public static KaproxyRequestMessageContainer getInstance() {
        return MqproxyRequestMessageContainerHolder.INSTANCE;
    }

    /**
     * Description:
     *
     * @author: ruanyi
     * @Date: 2018-09-04 19:46
     */
    private static class MqproxyRequestMessageContainerHolder {

        private static KaproxyRequestMessageContainer INSTANCE = new KaproxyRequestMessageContainer();

        private MqproxyRequestMessageContainerHolder() {

        }
    }
}
