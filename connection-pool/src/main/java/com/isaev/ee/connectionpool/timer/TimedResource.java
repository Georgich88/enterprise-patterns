package com.isaev.ee.connectionpool.timer;

import com.isaev.ee.connectionpool.connectionpool.ConnectionPool;
import com.isaev.ee.connectionpool.pool.PooledResource;
import com.isaev.ee.connectionpool.pool.PooledResourceState;

import java.util.Deque;

public class TimedResource<T> implements PooledResource<T> {

    private PooledResource<T> resource;
    private ResourceTimer<T> timer;

    public TimedResource(PooledResource resource, ConnectionPool<T> pool) {
        this.resource = resource;
        this.timer = new ResourceTimer<>(this, pool);
    }

    @Override
    public boolean startReleaseTest() {
        return resource.startReleaseTest();
    }

    @Override
    public boolean endReleaseTest(Deque<PooledResource<T>> idleQueue) {
        return resource.endReleaseTest(idleQueue);
    }

    @Override
    public boolean allocate() {
        return resource.allocate();
    }

    @Override
    public boolean deallocate() {
        return resource.deallocate();
    }

    @Override
    public PooledResourceState getState() {
        return resource.getState();
    }

    @Override
    public T getResource() {
        return resource.getResource();
    }

    @Override
    public long getCreationTime() {
        return resource.getCreationTime();
    }

    @Override
    public long getActiveTimeMillis() {
        return resource.getActiveTimeMillis();
    }

    @Override
    public long getBorrowedCount() {
        return resource.getBorrowedCount();
    }

    @Override
    public long getIdleTimeMillis() {
        return resource.getIdleTimeMillis();
    }

    @Override
    public long getLastBorrowTime() {
        return resource.getLastBorrowTime();
    }

    @Override
    public long getLastReturnTime() {
        return resource.getLastReturnTime();
    }

    @Override
    public long getLastUsedTime() {
        return resource.getLastUsedTime();
    }

    @Override
    public int compareTo(PooledResource<T> other) {
        return resource.compareTo(other);
    }
}
