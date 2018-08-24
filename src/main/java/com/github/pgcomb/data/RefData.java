package com.github.pgcomb.data;

import java.util.function.Function;

/**
 * Title: RefData <br>
 * Description: RefData <br>
 * Date: 2018年08月24日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class RefData<T> {

    private T data;

    public RefData(T data) {
        this.data = data;
    }

    public T get(){
        return data;
    }
    public void set(T data){
        this.data = data;
    }

    public synchronized void syncFunc(Function<T,T> function){
        data = function.apply(data);
    }
}
