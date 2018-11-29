package com.github.pgcomb.file;

import com.github.pgcomb.data.RefData;
import com.github.pgcomb.func.VoidFunc;
import com.github.pgcomb.io.IOUtil;
import com.github.pgcomb.pool.TaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Title: FileTask <br>
 * Description: FileTask <br>
 * Date: 2018年08月24日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class FileTask {

    private static final Logger log = LoggerFactory.getLogger(FileTask.class);

    private FileTask() {
    }

    /**
     * 按行处理一个文件的每行数据，需要输出文件
     *
     * @param task     处理器
     * @param src      源文件，会按行读取数据
     * @param dst      结果文件
     * @param function 对于每条数据的处理方法
     * @param writer   对于处理结果写文件前的处理
     * @param <R>      处理结果的返回类型
     */
    public static <R> void fileTask(TaskExecutor task, String src, String dst, Function<String, R> function, Function<R, String> writer) {
        try (BufferedReader br = IOUtil.newBR(src)) {
            //处理完成累计器
            RefData<Integer> integerRefData = new RefData<>(0);
            //所有任务累加器
            RefData<Integer> total = new RefData<>(0);
            //文件读取结束标识
            RefData<Boolean> end = new RefData<>(false);
            BufferedWriter bw = IOUtil.newBW(dst);
            //关闭流的方法
            VoidFunc close = () -> {
                if (integerRefData.get() >= total.get() && end.get()) {
                    try {
                        bw.close();
                    } catch (IOException e) {
                        log.error("FileTask#close",e);
                    }
                }
            };
            String a;
            while ((a = br.readLine()) != null) {
                total.syncFunc(data -> data + 1);
                task.asyncExec(new TaskExecutor.RunProp<String, R>(a) {
                    @Override
                    public R work(String data) {
                        return function.apply(data);
                    }
                }, str -> {
                    try {
                        bw.write(writer.apply(str));
                        bw.newLine();
                    } catch (IOException e) {
                        log.error("FileTask#fileTask:task.syncExec", e);
                    } finally {
                        integerRefData.syncFunc(data -> data + 1);
                        close.func();
                    }
                });
            }
            end.set(true);
            close.func();
        } catch (IOException e) {
            log.error("FileTask#fileTask", e);
        }
    }

    /**
     * 按行处理一个文件的每行数据,不需要输出文件
     *
     * @param task     处理器
     * @param src      源文件，会按行读取数据
     * @param function 对于每条数据的处理方法
     */
    public static void fileTask(TaskExecutor task, String src, Consumer<String> function) {
        try (BufferedReader br = IOUtil.newBR(src)) {
            String a;
            while ((a = br.readLine()) != null) {
                task.asyncExec(new TaskExecutor.RunProp<String, String>(a) {
                    @Override
                    public String work(String data) {
                        function.accept(data);
                        return null;
                    }
                });
            }
        } catch (IOException e) {
            log.error("FileTask#fileTask", e);
        }
    }
}
