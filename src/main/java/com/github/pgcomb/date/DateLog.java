package com.github.pgcomb.date;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DateLog {

    private static final Logger log = LoggerFactory.getLogger(DateLog.class);

    private static final String DEF_NAME = "_def";

    private DateLog() {}

    private static final ThreadNameMap DATES = new ThreadNameMap();

    public static void record() {
        record(DEF_NAME);
    }

    public static void record(String name) {
        recordS(Thread.currentThread(), name, null);
    }

    public static void record(ThreeFunc<LocalDateTime, LocalDateTime, Duration> threeFunc) {
        record(DEF_NAME, threeFunc);
    }

    public static void record(String name, ThreeFunc<LocalDateTime, LocalDateTime, Duration> threeFunc) {

        recordS(Thread.currentThread(), name, threeFunc);
    }

    public static void record(BiConsumer<LocalDateTime, LocalDateTime> biConsumer) {
        record(DEF_NAME, biConsumer);
    }

    public static void record(String name, BiConsumer<LocalDateTime, LocalDateTime> biConsumer) {
        record(name, (a, b, c) -> biConsumer.accept(a, b));
    }

    public static void record(Consumer<Duration> consumer) {
        record(DEF_NAME, consumer);
    }

    public static void record(String name, Consumer<Duration> consumer) {
        record(name, (a, b, c) -> consumer.accept(c));
    }

    private static void recordS(Thread thread, String name, ThreeFunc<LocalDateTime, LocalDateTime, Duration> threeFunc) {

        DATES.add(thread, name, new WindowLink<>(2, LocalDateTime.now()), LocalDateTime.now());
        WindowLink<LocalDateTime> windowLink = DATES.get(Thread.currentThread(), name);
        if (threeFunc != null && windowLink != null) {
            LocalDateTime dateTime0 = windowLink.get(0);
            LocalDateTime dateTime1 = windowLink.get(1);
            Duration between = Duration.between(dateTime1, dateTime0);
            threeFunc.func(dateTime0, dateTime1, between);
        }
    }

    @FunctionalInterface
    public interface ThreeFunc<A, B, C> {
        /**
         * 三个参数的方法
         *
         * @param a 第一个
         * @param b 第二个
         * @param c 第三个
         */
        void func(A a, B b, C c);
    }

    private static class ThreadNameMap {

        private ThreadNameMap() {
            ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                    new BasicThreadFactory.Builder().namingPattern("DateLog-del-pool-%d").daemon(true).build());
            executorService.scheduleAtFixedRate(this::del, 1, 5, TimeUnit.SECONDS);
        }

        private Map<Thread, Map<String, WindowLink<LocalDateTime>>> threadMapMap = new ConcurrentHashMap<>();

        private WindowLink<LocalDateTime> get(Thread thread, String name) {
            Map<String, WindowLink<LocalDateTime>> stringWindowLinkMap = threadMapMap.get(thread);
            if (stringWindowLinkMap == null) {
                return null;
            } else {
                return stringWindowLinkMap.get(name);
            }
        }

        private void add(Thread thread, String name, WindowLink<LocalDateTime> windowLink, LocalDateTime now) {
            threadMapMap.putIfAbsent(thread, new ConcurrentHashMap<>(1));
            threadMapMap.get(thread).putIfAbsent(name, windowLink);
            threadMapMap.get(thread).get(name).add(now);
        }

        private void del() {
            Set<Thread> threads = new HashSet<>(threadMapMap.keySet());
            threads.stream().filter(thread -> !thread.isAlive()).forEach(threadMapMap::remove);
            log.debug("ThreadNameMap#threadMapMap:{}",threadMapMap);
        }
    }
}
