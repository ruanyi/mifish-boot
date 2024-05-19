package com.ruanyi.mifish.mqproxy.consumer;

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
import com.ruanyi.mifish.mqproxy.MessageExecutorService;
import com.ruanyi.mifish.mqproxy.container.MqproxyConsumerMetaContainer;
import com.ruanyi.mifish.mqproxy.model.ConsumerStatus;
import com.ruanyi.mifish.mqproxy.model.MqproxyRequestMessage;
import com.ruanyi.mifish.mqproxy.model.MqproxyResponseMessage;
import com.ruanyi.mifish.mqproxy.model.QueueMessage;

/**
 * Description:
 * <p>
 * 一个MqproxyOkHttpConsumer，对应以下线程：
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
public class MqproxyOkHttpConsumer extends AbstractOkHttpConsumer {

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
     * MqproxyOkHttpConsumer
     *
     * @param messageExecutorService
     */
    public MqproxyOkHttpConsumer(MessageExecutorService messageExecutorService, int consumptionRate) {
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
            (mqproxyRequestMessage, mqproxyMessage) -> doConsumeMqproxyMessage(mqproxyRequestMessage, mqproxyMessage));
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
     * @param mqproxyRequestMessage
     * @param mqproxyMessage
     * @return void
     */
    private void doConsumeMqproxyMessage(MqproxyRequestMessage mqproxyRequestMessage, String mqproxyMessage) {
        try {
            // 反序列化序列化
            MqproxyResponseMessage mqproxyResponseMessage =
                JacksonUtils.json2Obj(mqproxyMessage, MqproxyResponseMessage.class);
            // 一般情况下，不存在value为空的情况
            if (mqproxyResponseMessage == null || !mqproxyResponseMessage.isSuccessful()
                || StringUtils.isBlank(mqproxyResponseMessage.getValue())) {
                LOG.error(Pair.of("clazz", "MqproxyOkHttpConsumer"), Pair.of("method", "doConsumeMqproxyMessage"),
                    Pair.of("group", mqproxyRequestMessage.getGroup()),
                    Pair.of("topic", mqproxyRequestMessage.getTopic()),
                    Pair.of("content", mqproxyResponseMessage.getValue()),
                    Pair.of("url", mqproxyRequestMessage.getUrl()), Pair.of("message", "message is blank"));
                return;
            }
            if (!MqproxyConsumerMetaContainer.getInstance().isContainConsumer(mqproxyRequestMessage.getGroup(),
                mqproxyResponseMessage.getTopic())) {
                // 一般情况下，不会发生
                LOG.error(Pair.of("clazz", "MqproxyOkHttpConsumer"), Pair.of("method", "doConsumeMqproxyMessage"),
                    Pair.of("group", mqproxyRequestMessage.getGroup()),
                    Pair.of("topic", mqproxyRequestMessage.getTopic()), Pair.of("key", mqproxyResponseMessage.getKey()),
                    Pair.of("msg", mqproxyResponseMessage.getValue()), Pair.of("message", "no cosumer"));
                return;
            }
            QueueMessage message = new QueueMessage(mqproxyRequestMessage.getGroup(), mqproxyResponseMessage.getTopic(),
                mqproxyResponseMessage.getPartition(), mqproxyResponseMessage.getValue());
            message.addAttribute(QueueMessage.OFFSET, mqproxyResponseMessage.getOffset());
            // add default error code
            message.addAttribute(RESP_ERROR_CODE, "RET_SUCCESS");
            message.addAttribute(RESP_DETAIL_ERROR_CODE, "000000");
            // 消费线程与业务线程拆开，避免相互影响
            this.messageExecutorService.submit(message);
        } catch (Exception ex) {
            // for now just log some message
            LOG.error(ex, Pair.of("clazz", "MqproxyOkHttpConsumer"), Pair.of("method", "doConsumeMqproxyMessage"),
                Pair.of("group", mqproxyRequestMessage.getGroup()), Pair.of("topic", mqproxyRequestMessage.getTopic()),
                Pair.of("mqproxyMessage", mqproxyMessage), Pair.of("message", "some error happend!"),
                Pair.of("url", mqproxyRequestMessage.getUrl()));
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
