package com.github.pgcomb.download.api;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * Title: PooledWrapperFactory <br>
 * Description: PooledWrapperFactory <br>
 * Date: 2018年09月12日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class PooledWrapperFactory<O> implements PooledObjectFactory<PoolObjectWrapper<O>> {

    private ObjectFactory<O> objFactory;

    private GenericObjectPool<PoolObjectWrapper<O>> genericObjectPool;

    public PooledWrapperFactory(ObjectFactory<O> objFactory) {
        this.objFactory = objFactory;
    }

    @Override
    public PooledObject<PoolObjectWrapper<O>> makeObject() throws Exception {
        O obj = objFactory.get();
        PoolObjectWrapper<O> ppw = new PoolObjectWrapper<>(obj, genericObjectPool);
        return new DefaultPooledObject<>(ppw);
    }

    @Override
    public void destroyObject(PooledObject<PoolObjectWrapper<O>> p) throws Exception {
        objFactory.peptic().destroyObject(p.getObject().get());
    }

    @Override
    public boolean validateObject(PooledObject<PoolObjectWrapper<O>> p) {
        return objFactory.peptic().validateObject(p.getObject().get());
    }

    @Override
    public void activateObject(PooledObject<PoolObjectWrapper<O>> p) throws Exception {
        objFactory.peptic().activateObject(p.getObject().get());
    }

    @Override
    public void passivateObject(PooledObject<PoolObjectWrapper<O>> p) throws Exception {
        objFactory.peptic().passivateObject(p.getObject().get());
    }

    public void setGenericObjectPool(GenericObjectPool<PoolObjectWrapper<O>> genericObjectPool){
        this.genericObjectPool = genericObjectPool;
    }
}
