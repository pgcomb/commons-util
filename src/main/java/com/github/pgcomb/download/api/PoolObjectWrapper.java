package com.github.pgcomb.download.api;

import org.apache.commons.pool2.impl.GenericObjectPool;

import java.io.Closeable;
import java.io.IOException;

/**
 * Title: PoolObjectWrapper <br>
 * Description: PoolObjectWrapper <br>
 * Date: 2018年09月12日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class PoolObjectWrapper<O> implements Wrapper,Closeable {

    private O obj;

    private GenericObjectPool<PoolObjectWrapper<O>> genericObjectPool;

    public PoolObjectWrapper(O obj, GenericObjectPool<PoolObjectWrapper<O>> genericObjectPool) {
        this.obj = obj;
        this.genericObjectPool = genericObjectPool;
    }

    public O get(){
        return obj;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) {
        if (iface.isInstance(obj)){
            return (T) obj;
        } else if (iface.isInstance(this)){
            return (T) this;
        }
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        if (iface.isInstance(obj) || iface.isInstance(this)){
            return true;
        }
        return false;
    }

    @Override
    public void close() throws IOException {
        genericObjectPool.returnObject(this);
    }
}
