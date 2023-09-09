package com.ruanyi.mifish.kaproxy.consumer;

import static com.ruanyi.mifish.common.utils.Constants.RESP_DETAIL_ERROR_CODE;
import static com.ruanyi.mifish.common.utils.Constants.RESP_ERROR_CODE;
import static java.net.HttpURLConnection.HTTP_NO_CONTENT;
import static java.net.HttpURLConnection.HTTP_OK;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.util.concurrent.RateLimiter;
import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.common.utils.JacksonUtils;
import com.ruanyi.mifish.kaproxy.container.KaproxyConsumerMetaContainer;
import com.ruanyi.mifish.kaproxy.message.MessageDigestEngine;
import com.ruanyi.mifish.kaproxy.model.KaproxyRequestMessage;
import com.ruanyi.mifish.kaproxy.model.KaproxyResponseMessage;
import com.ruanyi.mifish.kaproxy.model.QueueMessage;
import com.ruanyi.mifish.kaproxy.model.StartupConsumerMeta;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-11-17 22:20
 */
class OkHttpConsumerTask implements Runnable {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /** startupConsumerMeta */
    private StartupConsumerMeta startupConsumerMeta;

    /** okHttpClient 发起http请求 */
    private OkHttpClient okHttpClient = null;

    /** messageDigestEngine */
    private MessageDigestEngine messageDigestEngine;

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

    /**
     * KaproxyConsumerTask
     * 
     * @param startupConsumerMeta
     * @param okHttpClient
     */
    public OkHttpConsumerTask(StartupConsumerMeta startupConsumerMeta, OkHttpClient okHttpClient) {
        this.startupConsumerMeta = startupConsumerMeta;
        this.okHttpClient = okHttpClient;
    }

    /**
     * @see Runnable#run()
     */
    @Override
    public void run() {
        this.done = false;
        try {
            while (!this.stopRead) {
                List<KaproxyRequestMessage> kaproxyRequestMessages =
                    this.startupConsumerMeta.getKaproxyRequestMessages();
                for (KaproxyRequestMessage kaproxyRequestMessage : kaproxyRequestMessages) {
                    // 不允许抛出异常，如果消费失败了，休眠一段时间继续消费
                    Pair<Integer, String> httpResp = doOkHttpConsume(kaproxyRequestMessage);
                    this.lastPoll = new AtomicLong(System.currentTimeMillis());
                    // 如果返回的code == 200，进行反序列化
                    if (httpResp.getLeft() == 200) {
                        // 触发下一步业务处理
                        doConsumeKaproxyMessage(kaproxyRequestMessage, httpResp.getRight());
                    }
                    // 这边需要控制消费速度
                    Optional<RateLimiter> optionalRateLimiter = this.startupConsumerMeta.getOptionalRateLimiter();
                    if (optionalRateLimiter.isPresent()) {
                        RateLimiter rateLimiter = optionalRateLimiter.get();
                        rateLimiter.acquire();
                    }
                }
            }
            this.done = true;
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "OkHttpConsumerTask"), Pair.of("method", "run"),
                Pair.of("startupConsumerMeta", JacksonUtils.toJSONString(this.startupConsumerMeta)),
                Pair.of("message", "consumer pull message exception"));
            this.done = true;
        }
    }

    /**
     * doOkHttpConsume
     *
     * 不允许抛出异常
     *
     * @param kaproxyRequestMessage
     * @return void
     */
    private Pair<Integer, String> doOkHttpConsume(KaproxyRequestMessage kaproxyRequestMessage) {
        try {
            Request request = new Request.Builder().url(kaproxyRequestMessage.getUrl()).get().build();
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
                    Pair.of("group", kaproxyRequestMessage.getGroup()), Pair.of("httpcode", httpcode),
                    Pair.of("content", content), Pair.of("topic", kaproxyRequestMessage.getTopic()),
                    Pair.of("message", "unknow exception"), Pair.of("url", kaproxyRequestMessage.getUrl()));
            }
            // for now just log some error message
            if (LOG.isDebugEnabled()) {
                LOG.debug(Pair.of("clazz", "OkHttpConsumerTask"), Pair.of("method", "doOkHttpConsume"),
                    Pair.of("group", kaproxyRequestMessage.getGroup()),
                    Pair.of("topic", kaproxyRequestMessage.getTopic()), Pair.of("httpcode", httpcode),
                    Pair.of("content", content), Pair.of("url", kaproxyRequestMessage.getUrl()),
                    Pair.of("message", "consume one message from kaproxy"),
                    Pair.of("poolName", kaproxyRequestMessage.getPoolName()));
            }
            // return
            return Pair.of(httpcode, content);
        } catch (Exception ex) {
            // for now just log some message
            LOG.error(ex, Pair.of("clazz", "OkHttpConsumerTask"), Pair.of("method", "doOkHttpConsume"),
                Pair.of("group", kaproxyRequestMessage.getGroup()), Pair.of("topic", kaproxyRequestMessage.getTopic()),
                Pair.of("message", "may be io exception"), Pair.of("url", kaproxyRequestMessage.getUrl()));
            return Pair.of(-1, null);
        }
    }

    /**
     * doConsumeKaproxyMessage
     * <p>
     * 真实消费kafka队列中的消息，消费完后，提交给本地线程池处理线程池
     *
     * 不允许抛出异常
     *
     * @param kaproxyRequestMessage
     * @param kaproxyMessage
     * @return void
     */
    private void doConsumeKaproxyMessage(KaproxyRequestMessage kaproxyRequestMessage, String kaproxyMessage) {
        try {
            // 反序列化序列化
            KaproxyResponseMessage kaproxyResponseMessage =
                JacksonUtils.json2Obj(kaproxyMessage, KaproxyResponseMessage.class);
            // 一般情况下，不存在value为空的情况
            if (kaproxyResponseMessage == null || !kaproxyResponseMessage.isSuccessful()
                || StringUtils.isBlank(kaproxyResponseMessage.getValue())) {
                LOG.error(Pair.of("clazz", "OkHttpConsumerTask"), Pair.of("method", "doConsumeKaproxyMessage"),
                    Pair.of("group", kaproxyRequestMessage.getGroup()),
                    Pair.of("topic", kaproxyRequestMessage.getTopic()),
                    Pair.of("content", kaproxyResponseMessage.getValue()),
                    Pair.of("url", kaproxyRequestMessage.getUrl()), Pair.of("message", "message is blank"));
                return;
            }
            if (!KaproxyConsumerMetaContainer.getInstance().isContainConsumer(kaproxyRequestMessage.getGroup(),
                kaproxyResponseMessage.getTopic())) {
                // 一般情况下，不会发生
                LOG.error(Pair.of("clazz", "OkHttpConsumerTask"), Pair.of("method", "doConsumeKaproxyMessage"),
                    Pair.of("group", kaproxyRequestMessage.getGroup()),
                    Pair.of("topic", kaproxyRequestMessage.getTopic()), Pair.of("key", kaproxyResponseMessage.getKey()),
                    Pair.of("msg", kaproxyResponseMessage.getValue()), Pair.of("message", "no cosumer"));
                return;
            }
            QueueMessage message = new QueueMessage(kaproxyRequestMessage.getGroup(), kaproxyResponseMessage.getTopic(),
                kaproxyResponseMessage.getPartition(), kaproxyResponseMessage.getValue());
            message.addAttribute(QueueMessage.OFFSET, kaproxyResponseMessage.getOffset());
            // add default error code
            message.addAttribute(RESP_ERROR_CODE, "RET_SUCCESS");
            message.addAttribute(RESP_DETAIL_ERROR_CODE, "000000");
            // 触发业务处理
            this.messageDigestEngine.digest(KaproxyConsumerMetaContainer.getInstance()
                .getKaproxyConsumer(kaproxyRequestMessage.getGroup(), kaproxyResponseMessage.getTopic()), message);

        } catch (Exception ex) {
            // for now just log some message
            LOG.error(ex, Pair.of("clazz", "OkHttpConsumerTask"), Pair.of("method", "doConsumeKaproxyMessage"),
                Pair.of("group", kaproxyRequestMessage.getGroup()), Pair.of("topic", kaproxyRequestMessage.getTopic()),
                Pair.of("kaproxyMessage", kaproxyMessage), Pair.of("message", "some error happend!"),
                Pair.of("url", kaproxyRequestMessage.getUrl()));
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
     * setMessageDigestEngine
     *
     * @param messageDigestEngine
     */
    public void setMessageDigestEngine(MessageDigestEngine messageDigestEngine) {
        this.messageDigestEngine = messageDigestEngine;
    }
}
