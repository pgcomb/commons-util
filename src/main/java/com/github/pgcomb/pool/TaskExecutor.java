package com.github.pgcomb.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 分组多线程执行
 *
 * @author 王东旭
 */
public class TaskExecutor {

    private static final Logger log = LoggerFactory.getLogger(TaskExecutor.class);

    /**
     * 执行任务线程池
     */
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 回调线程池
     */
    private ThreadPoolExecutor threadPoolExecutorMain;

    private String name;

    private int maxPoolSize;

    public TaskExecutor(String name, int maxPoolSize, int queueCapacity) {
        this.name = name;
        this.maxPoolSize = maxPoolSize;
        threadPoolExecutor = PoolUtil.getPool(name, maxPoolSize, queueCapacity);
    }

    public void initMainPool(){
        if (threadPoolExecutorMain == null){
            threadPoolExecutorMain = PoolUtil.getPool(name + "-callback", maxPoolSize, Integer.MAX_VALUE);
        }
    }
    /**
     * 异步执行任务
     *
     * @param supplier 任务方法
     * @param callback 回调方法
     * @param <R>      任务回调的数据
     */
    public <R> void syncExec(Supplier<R> supplier, Consumer<R> callback) {
        initMainPool();
        threadPoolExecutor.submit(() -> {
            R r = supplier.get();
            threadPoolExecutorMain.submit(() -> callback.accept(r));
        });
    }

    public void syncExec(Runnable runnable) {
        threadPoolExecutor.submit(runnable);
    }

    /**
     * 多个带有回调的异步任务
     *
     * @param tasks    任务数据列表
     * @param taskFunc 任务方法
     * @param callback 回调
     * @param <T>      数据格式
     * @param <R>      回调数据
     */
    public <T, R> void syncExec(List<T> tasks, Function<T, R> taskFunc, Consumer<R> callback) {
        tasks.forEach(t -> syncExec(() -> taskFunc.apply(t), callback));
    }

    /**
     * 多个不带回调的异步任务
     *
     * @param tasks    任务数据列表
     * @param taskFunc 任务方法
     * @param <T>      数据格式
     */
    public <T> void syncExec(List<T> tasks, Consumer<T> taskFunc) {
        tasks.forEach(t -> syncExec(() -> taskFunc.accept(t)));
    }

    /**
     * 同步任务
     *
     * @param supplier 任务方法
     * @param <R>      返回数据格式
     * @return 执行结果
     */
    public <R> R asyncExec(Supplier<R> supplier) {
        Future<R> submit = threadPoolExecutor.submit(supplier::get);
        try {
            return submit.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("AsyncExec:get [error]", e);
            return null;
        }
    }

    /**
     * 同步任务
     *
     * @param <R> 返回数据格式
     * @return 执行结果
     */
    public <T, R> Map<T, R> asyncExec(List<T> list, Function<T, R> function) {

        Map<T, Future<R>> taskMap = list.stream()
                .collect(Collectors.toMap(k -> k, v -> threadPoolExecutor.submit(() -> {
                    //执行任务方法
                    return function.apply(v);
                })));

        Map<T, R> result = new HashMap<>(list.size());
        taskMap.forEach((k, v) -> {
            try {
                result.put(k, v.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error("TaskExecutor#asyncExec:Future.get()", e);
            }
        });
        return result;
    }

    public <R> Map<Integer, R> asyncExec(List<Supplier<R>> list) {
        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ints.add(i);
        }
        return asyncExec(ints, integer -> list.get(integer).get());
    }

    /**
     * 带有回调的分组任务
     * <p>
     * 一个任务集合执行完成后统一回调
     *
     * @param tasks    任务数据
     * @param taskFunc 任务方法
     * @param callback 回调
     * @param <T>      数据类型
     * @param <R>      回调数据类型
     */
    public <T, R> void syncGroupExec(List<T> tasks, Function<T, R> taskFunc, Consumer<Map<T, R>> callback) {
        initMainPool();
        //将任务装换成map  key:待处理的数据   value:处理的信息
        Map<T, TaskMsg<T, R>> taskMap = tasks.stream()
                .collect(Collectors.toMap(k -> k, TaskMsg::new));

        taskMap.forEach((T task, TaskMsg<T, R> taskMsg) -> {
            //依次提交任务到线程池
            Future<?> future = threadPoolExecutor.submit(() -> {
                        //执行任务方法
                        R apply = taskFunc.apply(task);
                        taskMsg.setCallBack(apply);
                        boolean isEnd;
                        //同一个任务组依次判断，只有都执行结束的时候才执行回调
                        synchronized (taskMap) {
                            taskMsg.end();
                            isEnd = taskMap.entrySet().stream().allMatch(mapEntity -> mapEntity.getValue().isEnd());
                        }
                        if (isEnd) {
                            Map<T, R> collect = taskMap.entrySet()
                                    .stream()
                                    .collect(Collectors.toMap(Map.Entry::getKey, entity -> entity.getValue().getCallBack()));
                            threadPoolExecutorMain.submit(() -> callback.accept(collect));
                        }
                    }
            );
            taskMsg.setFuture(future);
        });
    }

    /**
     * 带有回调的分组任务
     *
     * @param consumers 任务集合
     * @param callback  回调
     * @param <R>       回调数据类型
     */
    public <R> void syncGroupExec(List<Supplier<R>> consumers, Consumer<Map<Integer, R>> callback) {
        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < consumers.size(); i++) {
            ints.add(i);
        }
        syncGroupExec(ints, integer -> consumers.get(integer).get(), callback);
    }

    /**
     * 关闭线程池
     */
    public void stop() {
        new Thread(() -> {
            PoolUtil.poolStopSync(threadPoolExecutor);
            PoolUtil.poolStopSync(threadPoolExecutorMain);
        }).start();
    }

    /**
     * 任务信息类
     *
     * @param <T> 处理数据的类型
     * @param <R> 回调的数据类型
     */
    private class TaskMsg<T, R> {

        private R callBack;

        private T data;

        private Future future;

        private boolean isEnd = false;

        public TaskMsg(T data) {
            this.data = data;
        }

        public void setCallBack(R callBack) {
            this.callBack = callBack;
        }

        public void setFuture(Future future) {
            this.future = future;
        }

        public R getCallBack() {
            return callBack;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Future getFuture() {
            return future;
        }

        public boolean isEnd() {
            return isEnd;
        }

        public void end() {
            isEnd = true;
        }

        @Override
        public String toString() {
            return "TaskMsg{" +
                    "callBack=" + callBack +
                    ", data=" + data +
                    ", future=" + future +
                    ", isEnd=" + isEnd +
                    '}';
        }
    }
}
