package com.ruanyi.mifish.kaproxy.container;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.Lifecycle;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.kaproxy.MessageExecutorService;
import com.ruanyi.mifish.kaproxy.ProcessorConsumer;
import com.ruanyi.mifish.kaproxy.ProcessorConsumerContainer;
import com.ruanyi.mifish.kaproxy.StartupConsumerMetaFactory;
import com.ruanyi.mifish.kaproxy.consumer.KaproxyOkHttpConsumer;
import com.ruanyi.mifish.kaproxy.exceptions.ConsumerCreateException;
import com.ruanyi.mifish.kaproxy.exceptions.NoSuchConsumerException;
import com.ruanyi.mifish.kaproxy.model.ConsumerStartupStrategy;
import com.ruanyi.mifish.kaproxy.model.GroupTopic;
import com.ruanyi.mifish.kaproxy.model.StartupConsumerMeta;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2022-11-17 16:16
 */
public class KaproxyProcessorConsumerContainer
    implements ProcessorConsumerContainer, ApplicationListener<ContextRefreshedEvent>, EnvironmentAware {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    /** processorConsumers */
    private Map<GroupTopic, ProcessorConsumer> processorConsumers = new ConcurrentHashMap();

    /** startupConsumerMetaFactory */
    private StartupConsumerMetaFactory startupConsumerMetaFactory;

    /** messageExecutorService */
    private MessageExecutorService messageExecutorService;

    /** environment */
    private Environment environment;

    /** effectGroupTopics */
    private Set<GroupTopic> effectGroupTopics = new HashSet<>();

    /** running */
    private volatile boolean running = false;

    /**
     * KaproxyProcessorConsumerContainer
     *
     * @param effectGroupTopics
     * @param startupConsumerMetaFactory
     * @param messageExecutorService
     */
    public KaproxyProcessorConsumerContainer(String effectGroupTopics,
        StartupConsumerMetaFactory startupConsumerMetaFactory, MessageExecutorService messageExecutorService) {
        checkArgument(StringUtils.isNotBlank(effectGroupTopics),
            "effectGroupTopics cannot be blank in KaproxyProcessorConsumerContainer");
        checkArgument(startupConsumerMetaFactory != null,
            "StartupConsumerMetaFactory cannot be null in KaproxyProcessorConsumerContainer");
        checkArgument(messageExecutorService != null,
            "MessageExecutorService cannot be null in KaproxyProcessorConsumerContainer");
        this.effectGroupTopics.addAll(GroupTopic.parse2Set(effectGroupTopics));
        this.startupConsumerMetaFactory = startupConsumerMetaFactory;
        this.messageExecutorService = messageExecutorService;
    }

    /**
     * @see ProcessorConsumerContainer#getProcessorConsumer(GroupTopic)
     */
    @Override
    public ProcessorConsumer getProcessorConsumer(GroupTopic groupTopic) {
        return this.processorConsumers.get(groupTopic);
    }

    /**
     * @see ProcessorConsumerContainer#isContainConsumer(GroupTopic)
     */
    @Override
    public boolean isContainConsumer(GroupTopic groupTopic) {
        return this.processorConsumers.containsKey(groupTopic);
    }

    /**
     * @see ProcessorConsumerContainer#addProcessorConsumer(GroupTopic, ProcessorConsumer)
     */
    @Override
    public ProcessorConsumerContainer addProcessorConsumer(GroupTopic groupTopic, ProcessorConsumer processorConsumer) {
        if (groupTopic != null && processorConsumer != null) {
            this.processorConsumers.put(groupTopic, processorConsumer);
        }
        return this;
    }

    /**
     * @see ProcessorConsumerContainer#getAllConsumers()
     */
    @Override
    public Set<ProcessorConsumerContainer> getAllConsumers() {
        return new HashSet(this.processorConsumers.values());
    }

    /**
     * @see ApplicationListener#onApplicationEvent(ApplicationEvent)
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        start();
    }

    /**
     * @see EnvironmentAware#setEnvironment(Environment)
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
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
            ConsumerStartupStrategy strategy = obtainConsumerStartupStrategy();
            // queue consumer starting,not read
            List<StartupConsumerMeta> startupConsumerMetas =
                this.startupConsumerMetaFactory.batchBuild(this.effectGroupTopics, strategy);
            for (StartupConsumerMeta startupConsumerMeta : startupConsumerMetas) {
                KaproxyOkHttpConsumer kaproxyOkHttpConsumer =
                    new KaproxyOkHttpConsumer(startupConsumerMeta, this.messageExecutorService);
                // 注意：是策略优化的反向，再次放回容器中
                for (GroupTopic groupTopic : startupConsumerMeta.getGroupTopics()) {
                    this.processorConsumers.put(groupTopic, kaproxyOkHttpConsumer);
                }
            }
            // queue consumer start reading
            for (ProcessorConsumer queueConsumer : this.processorConsumers.values()) {
                // 启动，会校验对应的kafka是否真正启动
                queueConsumer.start();
                // 开始读
                queueConsumer.startRead();
            }
            this.running = true;
            LOG.warn(Pair.of("clazz", "KaproxyProcessorConsumerContainer"), Pair.of("method", "start"),
                Pair.of("effectGroupTopics", this.effectGroupTopics), Pair.of("running", this.running),
                Pair.of("size_of_consumers", this.processorConsumers.size()));
        } catch (ConsumerCreateException ex) {
            // 1、打印错误日志
            LOG.error(ex, Pair.of("clazz", "KaproxyProcessorConsumerContainer"), Pair.of("method", "start"),
                Pair.of("group", ex.getGroup()), Pair.of("topic", ex.getTopic()),
                Pair.of("message", "xiuxiu queue consumers cannot be started"), Pair.of("running", this.running));
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
            LOG.error(ex, Pair.of("clazz", "KaproxyProcessorConsumerContainer"), Pair.of("method", "start"),
                Pair.of("group", group), Pair.of("topic", topic), Pair.of("running", this.running),
                Pair.of("message", "xiuxiu queue consumers cannot be started"));
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
        // 再遍历一次，遍历是否已经真正停止
        for (Map.Entry<GroupTopic, ProcessorConsumer> entry : this.processorConsumers.entrySet()) {
            ProcessorConsumer queueConsumer = entry.getValue();
            queueConsumer.asyncStop();
        }
        // set running false
        this.running = false;
        // log message
        LOG.warn(Pair.of("clazz", "XiuxiuQueueConsumerContainer"), Pair.of("method", "stop"),
            Pair.of("effectGroupTopics", this.effectGroupTopics), Pair.of("running", this.running),
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
     * 1、环境变量：CONSUMER_STARTUP_STRATEGY
     * <p>
     * 2、系统属性中：CONSUMER_STARTUP_STRATEGY
     * <p>
     * 3、properties中：mifish.processor.kaproxy.consumer-startup-strategy
     * <p>
     * 4、如果都没有，则默认值：ConsumerStartupStrategy.COMBINE
     *
     * @return
     */
    private ConsumerStartupStrategy obtainConsumerStartupStrategy() {
        String strategy = System.getenv("CONSUMER_STARTUP_STRATEGY");
        if (StringUtils.isBlank(strategy)) {
            strategy = System.getProperty("CONSUMER_STARTUP_STRATEGY");
        }
        if (StringUtils.isBlank(strategy)) {
            strategy = this.environment.getProperty("mifish.processor.kaproxy.consumer-startup-strategy");
        }
        ConsumerStartupStrategy css = ConsumerStartupStrategy.of(strategy);
        if (css == null) {
            return ConsumerStartupStrategy.ALL_IN_ONE;
        }
        return css;
    }
}
