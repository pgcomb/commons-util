package com.github.pgcomb.func;

/**
 * Title: ThreeFunc <br>
 * Description: ThreeFunc <br>
 * Date: 2018年08月24日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */

@FunctionalInterface
public interface ThreeFunc<A, B, C> {
    /**
     * 三个参数的方法
     *
     * @param a 第一个
     * @param b 第二个
     * @param c 第三个
     */
    void func(A a, B b, C c);
}