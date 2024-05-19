package com.ruanyi.mifish.mqproxy.consumer;

import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.util.concurrent.RateLimiter;
import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.mqproxy.ProcessorConsumer;
import com.ruanyi.mifish.mqproxy.container.MqproxyRequestMessageContainer;
import com.ruanyi.mifish.mqproxy.model.MqproxyRequestMessage;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description:
 * 
 * 真正的消费线程，只管消费，不管具体的业务处理
 * 
 * 消费需支持：限流
 *
 * @author: ruanyi
 * @Date: 2022-11-17 22:20
 */
class OkHttpConsumerTask implements Runnable {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /** okHttpClient 发起http请求 */
    private OkHttpClient okHttpClient = null;

    /** 表示该任务是否已经结束 */
    private volatile boolean done = false;

    /** stopRead */
    private volatile boolean stopRead = false;

    /**
     * 注意：注意：刚启动时，设定的默认值是当前时间
     * <p>
     * 外面还有一守护线程，每隔30s,会去check当前状态
     */
    private AtomicLong lastPoll = new AtomicLong(System.currentTimeMillis());

    /** 从容器中，按照一定的规则拿出MqproxyRequestMessage */
    private MqproxyRequestMessageContainer mqproxyRequestMessageContainer =
        MqproxyRequestMessageContainer.getInstance();

    /** callback */
    private ProcessorConsumer.ConsumerCallback<MqproxyRequestMessage, String> callback;

    /**
     * 消费限流，不一定有，
     * 
     * 1、若没有配置，则不限流
     * 
     * 2、若有配置，则开启
     */
    private RateLimiter rateLimiter = null;

    /**
     * OkHttpConsumerTask
     * 
     * @param okHttpClient
     * @param callback
     */
    public OkHttpConsumerTask(OkHttpClient okHttpClient,
        ProcessorConsumer.ConsumerCallback<MqproxyRequestMessage, String> callback) {
        this.okHttpClient = okHttpClient;
        this.callback = callback;
    }

    /**
     * @see Runnable#run()
     */
    @Override
    public void run() {
        this.done = false;
        try {
            while (!this.stopRead) {
                // 目前的消费策略：随机选择其中的一个，经过一段时间后，基本上，可以平衡
                MqproxyRequestMessage mqproxyRequestMessage = this.mqproxyRequestMessageContainer.randomSelectOne();
                // 不允许抛出异常，如果消费失败了，休眠一段时间继续消费
                Pair<Integer, String> httpResp = doOkHttpConsume(mqproxyRequestMessage);
                this.lastPoll = new AtomicLong(System.currentTimeMillis());
                // 如果返回的code == 200，进行反序列化
                if (httpResp.getLeft() == 200) {
                    // 具体的业务处理
                    this.callback.callback(mqproxyRequestMessage, httpResp.getRight());
                }
                // 这边需要控制消费速度
                if (this.rateLimiter != null) {
                    rateLimiter.acquire();
                }
            }
            this.done = true;
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "OkHttpConsumerTask"), Pair.of("method", "run"),
                Pair.of("message", "consumer pull message exception"));
            this.done = true;
        }
    }

    /**
     * doOkHttpConsume
     *
     * 不允许抛出异常
     *
     * @param mqproxyRequestMessage
     * @return void
     */
    private Pair<Integer, String> doOkHttpConsume(MqproxyRequestMessage mqproxyRequestMessage) {
        try {
            Request request = new Request.Builder().url(mqproxyRequestMessage.getUrl()).get().build();
            String content = null;
            int httpcode = -1;
            try (Response response = this.okHttpClient.newCall(request).execute()) {
                httpcode = response.code();
                if (response.body() != null) {
                    content = response.body().string();
                }
            }
            // 200：证明消费正常，报文体里有消息
            // 204：证明:此时此刻，队列里没有消息
            if (httpcode != HTTP_OK && httpcode != HTTP_NO_CONTENT) {
                // for now just log some error message
                LOG.error(Pair.of("clazz", "OkHttpConsumerTask"), Pair.of("method", "doOkHttpConsume"),
                    Pair.of("group", mqproxyRequestMessage.getGroup()), Pair.of("httpcode", httpcode),
                    Pair.of("content", content), Pair.of("topic", mqproxyRequestMessage.getTopic()),
                    Pair.of("message", "unknow exception"), Pair.of("url", mqproxyRequestMessage.getUrl()));
            }
            // for now just log some error message
            if (LOG.isDebugEnabled()) {
                LOG.debug(Pair.of("clazz", "OkHttpConsumerTask"), Pair.of("method", "doOkHttpConsume"),
                    Pair.of("group", mqproxyRequestMessage.getGroup()),
                    Pair.of("topic", mqproxyRequestMessage.getTopic()), Pair.of("httpcode", httpcode),
                    Pair.of("content", content), Pair.of("url", mqproxyRequestMessage.getUrl()),
                    Pair.of("message", "consume one message from mqproxy"),
                    Pair.of("poolName", mqproxyRequestMessage.getPoolName()));
            }
            // return
            return Pair.of(httpcode, content);
        } catch (Exception ex) {
            // for now just log some message
            LOG.error(ex, Pair.of("clazz", "OkHttpConsumerTask"), Pair.of("method", "doOkHttpConsume"),
                Pair.of("group", mqproxyRequestMessage.getGroup()), Pair.of("topic", mqproxyRequestMessage.getTopic()),
                Pair.of("message", "may be io exception"), Pair.of("url", mqproxyRequestMessage.getUrl()));
            return Pair.of(-1, null);
        }
    }

    /**
     * getLastPoll
     *
     * @return
     */
    public AtomicLong getLastPoll() {
        return lastPoll;
    }

    /**
     * 判断该任务是否已经结束
     *
     * @return
     */
    public boolean isDone() {
        return this.done;
    }

    /**
     * stopRead 停止读消息，但是，已经读好的消息，则继续在业务线程池中进行处理，当前线程池可以退出了
     * <p>
     * 一旦停止，只能通过重启的方式，来设置成false
     *
     * @return void
     */
    public synchronized void stopRead() {
        this.stopRead = true;
    }

    /**
     * setRateLimiter
     * 
     * @param rateLimiter
     */
    public void setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }
}
