package com.github.pgcomb.func;

import java.util.function.Consumer;

/**
 * Title: ConsumerProp <br>
 * Description: ConsumerProp <br>
 * Date: 2018年08月24日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public abstract class ConsumerProp<T> implements Consumer<T> {

    private T data;

    public final void func(){
        accept(data);
    }
}
