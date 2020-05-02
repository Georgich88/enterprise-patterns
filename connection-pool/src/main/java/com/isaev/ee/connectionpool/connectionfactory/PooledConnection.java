package com.isaev.ee.connectionpool.connectionfactory;

import com.isaev.ee.connectionpool.pool.PooledResource;
import com.isaev.ee.connectionpool.pool.PooledResourceState;

import java.sql.Connection;
import java.util.Deque;

public class PooledConnection implements PooledResource<Connection> {

    private final Connection connection;
    private PooledResourceState state = PooledResourceState.IDLE;
    private final long createTime = System.currentTimeMillis();
    private volatile long lastBorrowTime = createTime;
    private volatile long lastUseTime = createTime;
    private volatile long lastReturnTime = createTime;
    private volatile long borrowedCount = 0;


    public PooledConnection(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getResource() {
        return connection;
    }

    @Override
    public long getCreationTime() {
        return createTime;
    }

    @Override
    public long getActiveTimeMillis() {
        // Take copies to avoid threading issues
        final long rTime = lastReturnTime;
        final long bTime = lastBorrowTime;

        if (rTime > bTime) {
            return rTime - bTime;
        }
        return System.currentTimeMillis() - bTime;
    }

    @Override
    public long getIdleTimeMillis() {
        final long elapsed = System.currentTimeMillis() - lastReturnTime;
        // elapsed may be negative if:
        // - another thread updates lastReturnTime during the calculation window
        // - System.currentTimeMillis() is not monotonic (e.g. system time is set back)
        return elapsed >= 0 ? elapsed : 0;
    }

    @Override
    public long getLastBorrowTime() {
        return lastBorrowTime;
    }

    @Override
    public long getLastReturnTime() {
        return lastReturnTime;
    }

    @Override
    public long getBorrowedCount() {
        return borrowedCount;
    }

    @Override
    public long getLastUsedTime() {
        return lastUseTime;
    }

    @Override
    public int compareTo(final PooledResource<Connection> other) {
        final long lastActiveDiff = this.getLastReturnTime() - other.getLastReturnTime();
        if (lastActiveDiff == 0) {
            return System.identityHashCode(this) - System.identityHashCode(other);
        }
        // handle int overflow
        return (int)Math.min(Math.max(lastActiveDiff, Integer.MIN_VALUE), Integer.MAX_VALUE);
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("Object: ");
        result.append(connection.toString());
        result.append(", State: ");
        synchronized (this) {
            result.append(state.toString());
        }
        return result.toString();
    }

    @Override
    public synchronized boolean startReleaseTest() {
        if (state == PooledResourceState.IDLE) {
            state = PooledResourceState.RELEASE;
            return true;
        }

        return false;
    }

    @Override
    public synchronized boolean endReleaseTest(final Deque<PooledResource<Connection>> idleQueue) {
        if (state == PooledResourceState.RELEASE) {
            state = PooledResourceState.IDLE;
            return true;
        }
        return false;
    }

    /**
     * Allocates the object.
     *
     * @return {@code true} if the original state was {@link PooledResourceState#IDLE IDLE}
     */
    @Override
    public synchronized boolean allocate() {
        if (state == PooledResourceState.IDLE) {
            state = PooledResourceState.ALLOCATED;
            lastBorrowTime = System.currentTimeMillis();
            lastUseTime = lastBorrowTime;
            borrowedCount++;
            return true;
        } else if (state == PooledResourceState.RELEASE) {
            return false;
        }
        return false;
    }

    @Override
    public synchronized boolean deallocate() {
        if (state == PooledResourceState.ALLOCATED) {
            state = PooledResourceState.IDLE;
            lastReturnTime = System.currentTimeMillis();
            return true;
        }

        return false;
    }


    /**
     * Returns the state of this object.
     * @return state
     */
    @Override
    public synchronized PooledResourceState getState() {
        return state;
    }




}
