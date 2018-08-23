package com.github.pgcomb.task;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Title: TimerUtil <br>
 * Description: TimerUtil <br>
 * Date: 2018年08月22日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class TimerUtil {

    public static void task(String name,List<Task> tasks){
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(tasks.size(),
                new BasicThreadFactory.Builder().namingPattern(name+"-pool-%d").daemon(true).build());
        tasks.forEach(task -> executorService.scheduleAtFixedRate(task.runnable, task.initialDelay, task.period, task.timeUnit));
    }

    public static void task(String name,long initialDelay, long period, TimeUnit timeUnit,Runnable runnable){
        task(name, Collections.singletonList(new Task(initialDelay, period, TimeUnit.SECONDS, runnable)));
    }

    public static void task(String name, long period, Runnable runnable){
        task(name, Collections.singletonList(new Task( period, runnable)));
    }

    public static TaskBuilder builder(){
        return new TaskBuilder();
    }

     public static class TaskBuilder{

        private List<Task> tasks = new ArrayList<>();

        private String name = "timer";

        public TaskBuilder add(long initialDelay, long period, TimeUnit timeUnit,Runnable runnable){
            tasks.add(new Task(initialDelay,period,timeUnit,runnable));
            return this;
        }

        public TaskBuilder add(long period,Runnable runnable){
            tasks.add(new Task(period,runnable));
            return this;
        }

        public TaskBuilder name(String name){
            this.name = name;
            return this;
        }

        public void build(){
            TimerUtil.task(name,tasks);
        }
    }

    public static class Task{

        private long initialDelay;

        private long period;

        private TimeUnit timeUnit = TimeUnit.SECONDS;

        private Runnable runnable;

        public Task(long initialDelay, long period, TimeUnit timeUnit, Runnable runnable) {
            this.initialDelay = initialDelay;
            this.period = period;
            this.timeUnit = timeUnit;
            this.runnable = runnable;
        }

        public Task(long period, Runnable runnable) {
            this.period = period;
            this.runnable = runnable;
        }

        public long getInitialDelay() {
            return initialDelay;
        }

        public long getPeriod() {
            return period;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        public Runnable getRunnable() {
            return runnable;
        }
    }
}
