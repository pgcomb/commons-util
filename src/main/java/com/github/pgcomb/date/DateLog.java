package com.github.pgcomb.date;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 记录时间间隔
 *
 * @author 王东旭
 */
public class DateLog {

    private static final  ThreadLocal<WindowLink<LocalDateTime>> DATES = new ThreadLocal<>();

    private static void init() {
        DATES.set(new WindowLink<>(2, LocalDateTime.now()));
    }

    private DateLog() {
    }

    public static void record(){
        if (DATES.get() == null){
            init();
        }
        DATES.get().add(LocalDateTime.now());
    }

    public static void record(ThreeFunc<LocalDateTime,LocalDateTime,Duration> threeFunc){
        if (DATES.get() == null){
            init();
        }
        WindowLink<LocalDateTime> localDateTimes = DATES.get();
        localDateTimes.add(LocalDateTime.now());
        LocalDateTime dateTime0 = localDateTimes.get(0);
        LocalDateTime dateTime1 = localDateTimes.get(1);
        Duration between = Duration.between(dateTime1,dateTime0);
        threeFunc.func(dateTime0,dateTime1,between);
    }

    public static void end(){
        DATES.remove();
    }

    @FunctionalInterface
    public interface ThreeFunc<A,B,C>{
        /**
         * 三个参数的方法
         *
         * @param a 第一个
         * @param b 第二个
         * @param c 第三个
         */
        void func(A a,B b,C c);
    }
}
