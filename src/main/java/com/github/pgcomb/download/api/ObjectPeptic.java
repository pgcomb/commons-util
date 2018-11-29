package com.github.pgcomb.download.api;

/**
 * Title: ObjectPeptic <br>
 * Description: ObjectPeptic <br>
 * Date: 2018年09月12日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public interface ObjectPeptic<O> {

    default void destroyObject(O p) throws Exception {}

    default boolean validateObject(O p){return false;}

    default void activateObject(O p) throws Exception{}

    default void passivateObject(O p) throws Exception{}
}
