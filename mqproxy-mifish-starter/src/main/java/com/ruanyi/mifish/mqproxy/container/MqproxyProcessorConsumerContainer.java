package com.ruanyi.mifish.mqproxy.container;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.Lifecycle;
import org.springframework.context.event.ContextRefreshedEvent;

import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.mqproxy.MessageExecutorService;
import com.ruanyi.mifish.mqproxy.MqproxyRequestMessageFactory;
import com.ruanyi.mifish.mqproxy.ProcessorConsumer;
import com.ruanyi.mifish.mqproxy.ProcessorConsumerContainer;
import com.ruanyi.mifish.mqproxy.consumer.MqproxyOkHttpConsumer;
import com.ruanyi.mifish.mqproxy.exceptions.ConsumerCreateException;
import com.ruanyi.mifish.mqproxy.exceptions.NoSuchConsumerException;
import com.ruanyi.mifish.mqproxy.model.MqproxyStartupMeta;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-11-17 16:16
 */
public class MqproxyProcessorConsumerContainer
    implements ProcessorConsumerContainer, ApplicationListener<ContextRefreshedEvent> {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /**
     * List里面的消费线程的个数，与：concurrency相对应
     */
    private List<ProcessorConsumer> processorConsumers = new ArrayList<>();

    /**
     * 业务处理线程池
     * 
     * 业务线程与消费线程是隔离的
     */
    private MessageExecutorService messageExecutorService;

    /** running */
    private volatile boolean running = false;

    /** mqproxyRequestMessageFactory */
    private MqproxyRequestMessageFactory mqproxyRequestMessageFactory;

    /** mqproxyStartupMeta */
    private MqproxyStartupMeta mqproxyStartupMeta;

    /**
     * MqProxyProcessorConsumerContainer
     *
     * @param mqproxyStartupMeta
     * @param mqproxyRequestMessageFactory
     * @param messageExecutorService
     */
    public MqproxyProcessorConsumerContainer(MqproxyStartupMeta mqproxyStartupMeta,
        MqproxyRequestMessageFactory mqproxyRequestMessageFactory, MessageExecutorService messageExecutorService) {
        checkArgument(mqproxyStartupMeta != null,
            "MqproxyStartupMeta cannot be null in MqProxyProcessorConsumerContainer");
        checkArgument(messageExecutorService != null,
            "MessageExecutorService cannot be null in MqProxyProcessorConsumerContainer");
        this.mqproxyStartupMeta = mqproxyStartupMeta;
        this.mqproxyRequestMessageFactory = mqproxyRequestMessageFactory;
        this.messageExecutorService = messageExecutorService;
    }

    /**
     * @see ApplicationListener#onApplicationEvent(ApplicationEvent)
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        start();
    }

    /**
     * @see Lifecycle#start()
     */
    @Override
    public synchronized void start() {
        try {
            if (this.running) {
                return;
            }
            String effectGroupTopics = this.mqproxyStartupMeta.getEffectGroupTopics();
            // 1、实例化mq proxy request message
            this.mqproxyRequestMessageFactory.batchBuild(effectGroupTopics);
            // 2、实例化多个MqproxyOkHttpConsumer
            for (int i = 0; i < this.mqproxyStartupMeta.getConsumeConcurrency(); i++) {
                MqproxyOkHttpConsumer mqproxyOkHttpConsumer = new MqproxyOkHttpConsumer(this.messageExecutorService,
                    this.mqproxyStartupMeta.getConsumptionRate());
                this.processorConsumers.add(mqproxyOkHttpConsumer);
                // 启动，会校验对应的kafka是否真正启动
                mqproxyOkHttpConsumer.start();
                // 开始读
                mqproxyOkHttpConsumer.startRead();
            }
            this.running = true;
            LOG.warn(Pair.of("clazz", "MqProxyProcessorConsumerContainer"), Pair.of("method", "start"),
                Pair.of("effectGroupTopics", effectGroupTopics), Pair.of("running", this.running),
                Pair.of("consumerThreadNum", this.processorConsumers.size()));
        } catch (ConsumerCreateException ex) {
            // 1、打印错误日志
            LOG.error(ex, Pair.of("clazz", "MqProxyProcessorConsumerContainer"), Pair.of("method", "start"),
                Pair.of("group", ex.getGroup()), Pair.of("topic", ex.getTopic()),
                Pair.of("message", "mq proxy processor queue consumers cannot be started"),
                Pair.of("running", this.running));
            // 2、停止目前已经启动好的：队列处理机
            stop();
            throw ex;
        } catch (Exception ex) {
            String group = "", topic = "";
            if (ex instanceof NoSuchConsumerException) {
                NoSuchConsumerException nex = (NoSuchConsumerException)ex;
                group = nex.getGroup();
                topic = nex.getTopic();
            }
            // 1、打印错误日志
            LOG.error(ex, Pair.of("clazz", "MqProxyProcessorConsumerContainer"), Pair.of("method", "start"),
                Pair.of("group", group), Pair.of("topic", topic), Pair.of("running", this.running),
                Pair.of("message", "queue consumers cannot be started"));
            // 2、停止目前已经启动好的：队列处理机
            stop();
            // 3、抛出异常，标记当前启动失败
            throw new ConsumerCreateException(group, topic, ex);
        }
    }

    /**
     * @see Lifecycle#stop()
     */
    @Override
    public synchronized void stop() {
        String effectGroupTopics = this.mqproxyStartupMeta.getEffectGroupTopics();
        // 再遍历一次，遍历是否已经真正停止
        for (ProcessorConsumer queueConsumer : this.processorConsumers) {
            queueConsumer.asyncStop();
        }
        // set running false
        this.running = false;
        // log message
        LOG.warn(Pair.of("clazz", "MqProxyProcessorConsumerContainer"), Pair.of("method", "stop"),
            Pair.of("effectGroupTopics", effectGroupTopics), Pair.of("running", this.running),
            Pair.of("size_of_consumers", this.processorConsumers.size()));

    }

    /**
     * @see ProcessorConsumerContainer#isRunning()
     */
    @Override
    public boolean isRunning() {
        return this.running;
    }

    /**
     * @see ProcessorConsumerContainer#getMqproxyRequestMessageFactory()
     */
    @Override
    public MqproxyRequestMessageFactory getMqproxyRequestMessageFactory() {
        return mqproxyRequestMessageFactory;
    }

    /**
     * @see ProcessorConsumerContainer#getAllConsumers()
     */
    @Override
    public List<ProcessorConsumer> getAllConsumers() {
        return new ArrayList<>(this.processorConsumers);
    }
}
