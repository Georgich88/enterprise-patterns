package com.isaev.ee.connectionpool.pool;

import java.util.Deque;

public interface PooledResource<T> extends Comparable<PooledResource<T>> {

    // State changing methods

    boolean startReleaseTest();

    boolean endReleaseTest(Deque<PooledResource<T>> idleQueue);

    boolean allocate();

    boolean deallocate();

    PooledResourceState getState();

    // Getters and setters

    T getResource();

    long getCreationTime();

    long getActiveTimeMillis();

    long getBorrowedCount();

    long getIdleTimeMillis();

    long getLastBorrowTime();

    long getLastReturnTime();

    long getLastUsedTime();

    // Object inherited methods

    @Override
    int compareTo(PooledResource<T> other);

    @Override
    int hashCode();

    @Override
    String toString();



}
