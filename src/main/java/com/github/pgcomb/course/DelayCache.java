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

    Supplier<T> supplier();

    T obtain();
}
