package com.ruanyi.mifish.kaproxy.consumer;

import static com.ruanyi.mifish.common.utils.Constants.RESP_DETAIL_ERROR_CODE;
import static com.ruanyi.mifish.common.utils.Constants.RESP_ERROR_CODE;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.util.concurrent.RateLimiter;
import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.common.utils.JacksonUtils;
import com.ruanyi.mifish.kaproxy.MessageExecutorService;
import com.ruanyi.mifish.kaproxy.container.KaproxyConsumerMetaContainer;
import com.ruanyi.mifish.kaproxy.model.ConsumerStatus;
import com.ruanyi.mifish.kaproxy.model.KaproxyRequestMessage;
import com.ruanyi.mifish.kaproxy.model.KaproxyResponseMessage;
import com.ruanyi.mifish.kaproxy.model.QueueMessage;

/**
 * Description:
 * <p>
 * 一个KaproxyOkHttpConsumer，对应以下线程：
 * <p>
 * 1、一个：OkHttpConsumerTask，它是真正消费的业务线程
 * <p>
 * 2、停止消费时：会启动一个停止消费的线程。
 * <p>
 * 而spring kafka中的concurrency,并非对应：OkHttpConsumerTask,而是对应：
 *
 * @author: ruanyi
 * @Date: 2022-11-17 16:09
 */
public class KaproxyOkHttpConsumer extends AbstractOkHttpConsumer {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /** executors */
    private ExecutorService executors = null;

    /** messageExecutorService */
    private MessageExecutorService messageExecutorService;

    /**
     * 目前，一个StartupConsumerMeta 对应一个OkHttpConsumerTask
     * 
     * 等后面有其他业务场景，再重新设计
     */
    private OkHttpConsumerTask okHttpConsumerTask;

    /**
     * 消费速率
     *
     * 通过guava RateLimiter来实现，单机版
     *
     * 每秒消费多少个
     *
     * -1：代表没有限制
     */
    private int consumptionRate = -1;

    /**
     * KaproxyOkHttpConsumer
     *
     * @param messageExecutorService
     */
    public KaproxyOkHttpConsumer(MessageExecutorService messageExecutorService, int consumptionRate) {
        this.messageExecutorService = messageExecutorService;
        this.consumptionRate = consumptionRate;
    }

    /**
     * @see AbstractOkHttpConsumer#doInnerStart()
     */
    @Override
    public void doInnerStart() throws Exception {
        long start = System.currentTimeMillis();
        // 启动和停止公用一个锁,如果前一个stop操作还没完成则会一直等待不会冲突
        this.status = ConsumerStatus.STARTING;
        // 启动线程池去消费
        this.executors =
            new ThreadPoolExecutor(1, 2, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<>(), (Runnable runnable) -> {
                Thread thread = new Thread(runnable);
                thread.setName("mqproxy-consumer-featcher-%d");
                return thread;
            });
        this.okHttpConsumerTask = new OkHttpConsumerTask(this.getOkHttpClient(),
            (kaproxyRequestMessage, mqproxyMessage) -> doConsumeMqproxyMessage(kaproxyRequestMessage, mqproxyMessage));
        if (this.consumptionRate > 0) {
            RateLimiter rateLimiter = RateLimiter.create(this.consumptionRate);
            this.okHttpConsumerTask.setRateLimiter(rateLimiter);
        }
        // 标记处于Running状态
        this.status = ConsumerStatus.RUNNING;
        if (LOG.isInfoEnabled()) {
            LOG.info(Pair.of("clazz", "MqproxyOkHttpConsumer"), Pair.of("method", "start"),
                Pair.of("status", this.status), Pair.of("cost", (System.currentTimeMillis() - start)),
                Pair.of("message", "start the consumer successfully"));
        }
    }

    /**
     * doConsumeMqproxyMessage
     * <p>
     * 真实消费kafka队列中的消息，消费完后，提交给本地线程池处理线程池
     *
     * 不允许抛出异常
     *
     * @param kaproxyRequestMessage
     * @param mqproxyMessage
     * @return void
     */
    private void doConsumeMqproxyMessage(KaproxyRequestMessage kaproxyRequestMessage, String mqproxyMessage) {
        try {
            // 反序列化序列化
            KaproxyResponseMessage kaproxyResponseMessage =
                JacksonUtils.json2Obj(mqproxyMessage, KaproxyResponseMessage.class);
            // 一般情况下，不存在value为空的情况
            if (kaproxyResponseMessage == null || !kaproxyResponseMessage.isSuccessful()
                || StringUtils.isBlank(kaproxyResponseMessage.getValue())) {
                LOG.error(Pair.of("clazz", "MqproxyOkHttpConsumer"), Pair.of("method", "doConsumeMqproxyMessage"),
                    Pair.of("group", kaproxyRequestMessage.getGroup()),
                    Pair.of("topic", kaproxyRequestMessage.getTopic()),
                    Pair.of("content", kaproxyResponseMessage.getValue()),
                    Pair.of("url", kaproxyRequestMessage.getUrl()), Pair.of("message", "message is blank"));
                return;
            }
            if (!KaproxyConsumerMetaContainer.getInstance().isContainConsumer(kaproxyRequestMessage.getGroup(),
                kaproxyResponseMessage.getTopic())) {
                // 一般情况下，不会发生
                LOG.error(Pair.of("clazz", "MqproxyOkHttpConsumer"), Pair.of("method", "doConsumeMqproxyMessage"),
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
            // 消费线程与业务线程拆开，避免相互影响
            this.messageExecutorService.submit(message);
        } catch (Exception ex) {
            // for now just log some message
            LOG.error(ex, Pair.of("clazz", "MqproxyOkHttpConsumer"), Pair.of("method", "doConsumeMqproxyMessage"),
                Pair.of("group", kaproxyRequestMessage.getGroup()), Pair.of("topic", kaproxyRequestMessage.getTopic()),
                Pair.of("mqproxyMessage", mqproxyMessage), Pair.of("message", "some error happend!"),
                Pair.of("url", kaproxyRequestMessage.getUrl()));
        }
    }

    /**
     * @see AbstractOkHttpConsumer#doInnerStop(boolean)
     */
    @Override
    public void doInnerStop(boolean async) throws Exception {
        if (isStarting() || isRunning() || isReading()) {
            this.status = ConsumerStatus.STOPPING;
            if (async) {
                this.executors.submit(() -> {
                    stopReadMqproxy();
                    status = ConsumerStatus.STOPPED;
                });
            } else {
                stopReadMqproxy();
                status = ConsumerStatus.STOPPED;
            }
            if (LOG.isInfoEnabled()) {
                LOG.info(Pair.of("clazz", "MqproxyOkHttpConsumer"), Pair.of("method", "stop"),
                    Pair.of("status", this.status), Pair.of("async", async),
                    Pair.of("message", "Stoping the topic consumer"));
            }
        }
    }

    /**
     * @see AbstractOkHttpConsumer#doInnerStartRead()
     */
    @Override
    protected void doInnerStartRead() {
        // 只是将消费任务，提交给线程池：start mqproxy stream consumer task
        this.executors.submit(this.okHttpConsumerTask);
    }

    /**
     * 停止读mqproxy
     *
     * @return
     */
    private void stopReadMqproxy() {
        // 停止读
        this.okHttpConsumerTask.stopRead();
        // 关闭线程池
        this.executors.shutdown();
        LOG.warn(Pair.of("clazz", "MqproxyOkHttpConsumer"), Pair.of("method", "stopReadMqproxy"),
            Pair.of("status", this.status), Pair.of("message", "stop read message queue proxy stream success"));
    }
}
