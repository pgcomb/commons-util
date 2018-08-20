package com.github.pgcomb.date;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DateLog {

    private static final String DEF_NAME = "_def";
    public DateLog() {
    }

    private static final  ThreadLocal<Map<String,WindowLink<LocalDateTime>>> DATES = new ThreadLocal<>();

    private static void init(String name) {
        if (DATES.get() == null){
            Map<String,WindowLink<LocalDateTime>> objects = new HashMap<>(1);
            DATES.set(objects);
        }
        DATES.get().put(name,new WindowLink<>(2,LocalDateTime.now()));
    }

    public static void record(){
        record(DEF_NAME);
    }

    public static void record(String name){
        if (DATES.get() == null || DATES.get().get(name) == null){
            init(name);
        }
        DATES.get().get(name).add(LocalDateTime.now());
    }
    public static void record(ThreeFunc<LocalDateTime,LocalDateTime,Duration> threeFunc){
        record(DEF_NAME,threeFunc);
    }
    public static void record(String name, ThreeFunc<LocalDateTime,LocalDateTime,Duration> threeFunc){
        if (DATES.get() == null || DATES.get().get(name) == null){
            init(name);
        }
        WindowLink<LocalDateTime> localDateTimes = DATES.get().get(name);
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
