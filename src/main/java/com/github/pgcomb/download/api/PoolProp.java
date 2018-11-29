package com.github.pgcomb.download.api;

/**
 * Title: PoolProp <br>
 * Description: PoolProp <br>
 * Date: 2018年09月12日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public interface PoolProp {

    int maxTotal();

    int maxIdle();

    int minIdle();

}
