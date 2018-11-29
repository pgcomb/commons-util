package com.github.pgcomb.download.api;

/**
 * Title: Wrapper <br>
 * Description: Wrapper <br>
 * Date: 2018年09月12日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public interface Wrapper {

    <T> T unwrap(Class<T> iface);

    boolean isWrapperFor(Class<?> iface);
}
