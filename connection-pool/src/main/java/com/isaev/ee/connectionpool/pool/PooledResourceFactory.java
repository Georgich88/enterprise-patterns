package com.isaev.ee.connectionpool.pool;

public interface PooledResourceFactory<T> {

    PooledResource<T> createObject() throws Exception;

    void destroyObject(PooledResource<T> p) throws Exception;

    void activateObject(PooledResource<T> p) throws Exception;

    void deactivateObject(PooledResource<T> p) throws Exception;

}
