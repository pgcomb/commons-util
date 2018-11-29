package com.github.pgcomb.download.api;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title: PoolManager <br>
 * Description: PoolManager <br>
 * Date: 2018年09月12日
 *
 * @author 王东旭
 * @version 1.0.0
 * @since jdk8
 */
public class PoolManager<O> {

    private static final Logger log = LoggerFactory.getLogger(PoolManager.class);

    private GenericObjectPool<PoolObjectWrapper<O>> genericObjectPool;

    public PoolManager(ObjectFactory<O> pf, PoolProp poolProp) {
        GenericObjectPoolConfig<PoolObjectWrapper<O>> pgoc = new GenericObjectPoolConfig<>();
        pgoc.setMaxTotal(poolProp.maxTotal());
        pgoc.setMaxIdle(poolProp.maxIdle());
        pgoc.setMinIdle(poolProp.minIdle());

        PooledWrapperFactory<O> pwf = new PooledWrapperFactory<>(pf);
        genericObjectPool = new GenericObjectPool<>(pwf,pgoc);
        pwf.setGenericObjectPool(genericObjectPool);
    }

    public PoolObjectWrapper<O> getObject(){
        try {
            return genericObjectPool.borrowObject();
        } catch (Exception e) {
            log.error("",e);
            return null;
        }
    }

    public GenericObjectPool<PoolObjectWrapper<O>> pool(){
        return genericObjectPool;
    }
}
