package com.github.pgcomb.download.api;

import com.github.pgcomb.download.exception.BolterException;

/**
 * Title: ObjectFactory <br>
 * Description: ObjectFactory <br>
 * Date: 2018年09月12日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public interface ObjectFactory<T> {

    T get() throws BolterException;

    ObjectPeptic<T> peptic();
}
