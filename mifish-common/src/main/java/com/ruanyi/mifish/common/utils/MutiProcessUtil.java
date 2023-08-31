package com.ruanyi.mifish.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.common.model.ProcessResult;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2022-11-23 13:54
 */
public final class MutiProcessUtil {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.processor;

    /**
     * 正在执行中的进程信息<br>
     * <p>
     * 理论上，缓存中的，进程数量，不会超过5个
     */
    private static final Map<String, Process> PROCESS_CACHE = Maps.newConcurrentMap();

    /** LOG_SUB_PROCESS_MSG_THREAD_POOL */
    private static final ThreadPoolExecutor LOG_SUB_PROCESS_MSG_THREAD_POOL =
        new ThreadPoolExecutor(2, 10, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2),
            new ThreadFactoryBuilder().setNameFormat("log-subprocess-msg-thread-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());

    /** MutiProcessUtil */
    private MutiProcessUtil() {

    }

    /**
     * killProcess
     *
     * @param pid
     * @return
     */
    public static int killProcess(String pid) {
        if (PROCESS_CACHE.containsKey(pid)) {
            Process process = PROCESS_CACHE.get(pid);
            process.destroy();
            return 0;
        }
        // 证明此进程，不存在
        return -2;
    }

    /**
     * runShellProcess
     *
     * @param pid
     * @param cmd
     * @return
     */
    public static ProcessResult runShellProcess(String pid, String cmd) {
        return runSubProcess(pid, 60, TimeUnit.MINUTES, "sh", "-c", cmd);
    }

    /**
     * 默认超时时间是1h
     *
     * @param pid
     * @param cmd
     * @return
     */
    public static ProcessResult runSubProcess(String pid, String... cmd) {
        return runSubProcess(pid, 60, TimeUnit.MINUTES, cmd);
    }

    /**
     * pid，有外面控制，唯一id
     *
     * @param pid
     * @param timeout
     * @param unit
     * @param cmd
     * @return
     */
    public static ProcessResult runSubProcess(String pid, long timeout, TimeUnit unit, String... cmd) {
        // 进程pid，已经存在
        if (PROCESS_CACHE.containsKey(pid)) {
            return ProcessResult.ALREADY_EXISTS(pid, cmd);
        }
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        // 启动一子进程
        Process process = null;
        synchronized (MutiProcessUtil.class) {
            // 进程pid，已经存在
            if (PROCESS_CACHE.containsKey(pid)) {
                return ProcessResult.ALREADY_EXISTS(pid, cmd);
            }
            // 由于k8s容器内会不定时抛出open /dev/null: operation not permitted 的异常，因此这里增加两次重试
            for (int i = 0; i < 3; i++) {
                try {
                    process = processBuilder.start();
                    if (process != null) {
                        // 如果进程已启动，并正常运行。直接退出break
                        PROCESS_CACHE.put(pid, process);
                        break;
                    }
                } catch (IOException ex) {
                    LOG.error(ex, Pair.of("clazz", "MutiProcessUtil"), Pair.of("method", "runSubProcess"),
                        Pair.of("pid", pid), Pair.of("cmd", cmd), Pair.of("curr_cnt", i),
                        Pair.of("message", "start a subprocess error"));
                    if (i == 2) {
                        return ProcessResult.FAIL(pid, -2, cmd);
                    }
                }
            }
        }
        // 等待进程执行结果
        try {
            ProcessResult processResult = new ProcessResult();
            processResult.setPid(pid);
            processResult.setCmdStr(Arrays.toString(cmd));
            // std out //std err 目前仅仅是打印日志
            LogLineMsg stdoutMsg = new LogLineMsg(process.getInputStream(), processResult, true);
            LOG_SUB_PROCESS_MSG_THREAD_POOL.submit(stdoutMsg);
            LogLineMsg stderrMsg = new LogLineMsg(process.getErrorStream(), processResult, false);
            LOG_SUB_PROCESS_MSG_THREAD_POOL.submit(stderrMsg);
            // 其实，当算法输出
            boolean flag = process.waitFor(timeout, unit);
            // 如果还没执行完毕，把子进程直接kill 掉
            if (!flag) {
                process.destroy();
                PROCESS_CACHE.remove(pid);
                // 超时退出
                return ProcessResult.FAIL(pid, -8, cmd);
            }
            // 获取进程的退出
            PROCESS_CACHE.remove(pid);
            // 获取子进程退出的数值
            processResult.setExitValue(process.exitValue());
            return processResult;
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "MutiProcessUtil"), Pair.of("method", "runSubProcess"), Pair.of("pid", pid),
                Pair.of("cmd", cmd), Pair.of("message", "start a subprocess error"));
            return ProcessResult.FAIL(pid, -9, cmd);
        }
    }

    /**
     * Description:
     *
     * @author: rls
     * @Date: 2022-11-23 13:54
     */
    private static final class LogLineMsg implements Runnable {

        /** inputStream */
        private InputStream inputStream;

        /** processResult */
        private ProcessResult processResult;

        /** isStdout */
        private boolean isStdout;

        /**
         * LogLineMsg
         *
         * @param inputStream
         * @param processResult
         * @param isStdout
         */
        public LogLineMsg(InputStream inputStream, ProcessResult processResult, boolean isStdout) {
            this.inputStream = inputStream;
            this.processResult = processResult;
            this.isStdout = isStdout;
        }

        /**
         * @see Runnable#run()
         */
        @Override
        public void run() {
            try {
                // 这里的bytes.Buffer是动态空间的，内置的byte数组会自动调整。否则如果buffer的缓冲区满了，会阻塞住function进程的输出。
                BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                StringBuilder result = new StringBuilder();
                while ((line = bufferReader.readLine()) != null) {
                    result.append(line);
                    if (LOG.isInfoEnabled()) {
                        LOG.info(Pair.of("clazz", "LogLineMsg"), Pair.of("method", "run"),
                            Pair.of("pid", processResult.getPid()), Pair.of("output", line),
                            Pair.of("cmdStr", processResult.getCmdStr()));
                    }
                }
                //
                if (isStdout) {
                    processResult.setStdout(result.toString());
                } else {
                    processResult.setStderr(result.toString());
                }
            } catch (Exception ex) {
                // ingore
                LOG.error(ex, Pair.of("clazz", "LogLineMsg"), Pair.of("method", "run"),
                    Pair.of("pid", processResult.getPid()), Pair.of("message", "print error msg"),
                    Pair.of("cmdStr", processResult.getCmdStr()));
            }
        }
    }
}
