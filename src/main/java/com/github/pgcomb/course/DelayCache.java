package com.github.pgcomb.course;

import java.util.function.Supplier;

/**
 * Title: DelayCache <br>
 * Description: DelayCache <br>
 * Date: 2018年08月03日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public interface DelayCache<T> {

    /**
     * 数据提供接口
     *
     * @return 数据提供者
     */
    Supplier<T> supplier();

    /**
     * 获取接口，使用该接口提供服务
     *
     * @return 数据
     */
    T obtain();
}
