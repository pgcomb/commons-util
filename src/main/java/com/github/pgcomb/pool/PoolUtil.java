package com.github.pgcomb.pool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Title: ExecPool <br>
 * Description: 线程池工具类 <br>
 * Date: 2018年07月26日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class PoolUtil {

    private static final Logger log = LoggerFactory.getLogger(PoolUtil.class);

    /**
     * 当溢出时阻塞到队列任务数下降到队列总大小的百分比
     */
    private static final float OVERFLOW_WAIT = 0.8f;

    /**
     * 默认线程池活跃的与最大数量的比例
     */
    private static final float MAX_CORE_SIZE_RATIO = 0.5f;

    /**
     * 空闲线程默认存活的时间
     */
    private static final int KEEP_ACTIVE = 2;

    /**
     * 获取一个线程池
     *
     * @param name          名称
     * @param corePoolSize  corePoolSize
     * @param maxPoolSize   maxPoolSize
     * @param keepActiveS   keepActiveS
     * @param queueCapacity queueCapacity
     * @return ThreadPoolExecutor
     */
    public static ThreadPoolExecutor getPool(String name, int corePoolSize, int maxPoolSize, int keepActiveS, int queueCapacity) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(name + "-pool-%d").build();
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepActiveS, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueCapacity), namedThreadFactory, (r, executor1) -> {
            try {
                while (executor1.getQueue().size() > queueCapacity * OVERFLOW_WAIT) {
                    Thread.sleep(50L);
                }
            } catch (InterruptedException e) {
                log.error(name + "-pool:sleep is false", e);
                Thread.currentThread().interrupt();
            }
            executor1.submit(r);
        });
    }

    public static ThreadPoolExecutor getPool(String name, int maxPoolSize, int queueCapacity) {
        int corePoolSize = (int) (maxPoolSize * MAX_CORE_SIZE_RATIO);
        corePoolSize = corePoolSize == 0 ? 1 : corePoolSize;
        return getPool(name, corePoolSize, maxPoolSize, KEEP_ACTIVE, queueCapacity);
    }

    /**
     * 关闭线程池（阻塞）
     *
     * @param executor 线程池
     */
    public static void poolStopSync(ExecutorService executor) {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            try {
                while (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                    log.debug("wait for the thread pool task to end.");
                }
            } catch (InterruptedException e) {
                log.error("close pool is error!", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 关闭线程池（非阻塞）
     *
     * @param executor 线程池
     */
    public static void poolStopAsync(ExecutorService executor) {
        new Thread(() -> poolStopSync(executor)).start();
    }


}
