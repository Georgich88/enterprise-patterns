package com.isaev.ee.connectionpool.pool;

import java.util.Deque;

public interface PooledResource<T> extends Comparable<PooledResource<T>> {

    // Accessors

    T getResource();

    long getCreationTime();

    long getActiveTimeMillis();

    long getBorrowedCount();

    long getIdleTimeMillis();

    long getLastBorrowTime();

    long getLastReturnTime();

    long getLastUsedTime();

    // State changing methods

    boolean startReleaseTest();

    boolean endReleaseTest(Deque<PooledResource<T>> idleQueue);

    boolean allocate();

    boolean deallocate();

    PooledResourceState getState();

    // Inherited methods

    @Override
    int compareTo(PooledResource<T> other);

    @Override
    int hashCode();

    @Override
    String toString();



}
