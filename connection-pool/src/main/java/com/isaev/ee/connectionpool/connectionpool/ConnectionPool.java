package com.isaev.ee.connectionpool.connectionpool;

import com.isaev.ee.connectionpool.pool.PooledResource;
import com.isaev.ee.connectionpool.pool.PooledResourceFactory;
import com.isaev.ee.connectionpool.pool.ResourcePool;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;

public class ConnectionPool<T> implements ResourcePool<T> {

    // Constructors

    public ConnectionPool(final PooledResourceFactory<T> factory) {
        this(factory, new ConnectionPoolConfig<T>());
    }

    public ConnectionPool(final PooledResourceFactory<T> factory,
                          final ConnectionPoolConfig<T> config) {

        if (factory == null) {
            throw new IllegalArgumentException("Factory may not be null");
        }
        this.factory = factory;
        idleObjects = new LinkedBlockingDeque<>();
        setConfig(config);
    }

    // Configuration attributes
    private volatile int maxTotal = ConnectionPoolConfig.DEFAULT_MAX_TOTAL;
    private volatile int maxIdle = ConnectionPoolConfig.DEFAULT_MAX_IDLE;
    private volatile int minIdle = ConnectionPoolConfig.DEFAULT_MIN_IDLE;
    private volatile long maxWaitMillis = ConnectionPoolConfig.DEFAULT_MAX_WAIT_MILLIS;
    private final PooledResourceFactory<T> factory;

    // Internal attributes
    private final Map<IdentityWrapper<T>, PooledResource<T>> allObjects = new ConcurrentHashMap<>();
    private final AtomicLong createCount = new AtomicLong(0);
    private final AtomicLong destroyedCount = new AtomicLong(0);
    private long createResourceCount = 0;
    private final Object createResourceCountLock = new Object();
    private final LinkedBlockingDeque<PooledResource<T>> idleObjects;


    public void setConfig(final ConnectionPoolConfig<T> config) {
        //super.setConfig(conf);
        setMaxIdle(config.getMaxIdle());
        setMinIdle(config.getMinIdle());
        setMaxTotal(config.getMaxTotal());
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    // Resource pool methods.

    @Override
    public void addResource() throws Exception, IllegalAccessError, UnsupportedOperationException {
        if (factory == null) {
            throw new IllegalArgumentException("Cannot add resource. Factory is not initialized.");
        }
        PooledResource<T> resource = create();
        addIdleResource(resource);
    }

    @Override
    public T borrowResource() throws Exception, NoSuchElementException, IllegalStateException {
        return null;
    }

    @Override
    public void clear() throws Exception, UnsupportedOperationException {

    }

    @Override
    public void close() {

    }

    @Override
    public int getActiveNumber() {
        return 0;
    }

    @Override
    public int getIdleNumber() {
        return 0;
    }

    @Override
    public void releaseResource(T resource) throws Exception {

    }

    @Override
    public void returnResource(T resource) throws Exception {
        final PooledResource<T> pooledResource = allObjects.get(new IdentityWrapper<>(resource));
        if (pooledResource == null) {
            throw new IllegalStateException("Invalidated object not currently part of this pool");
        }
        synchronized (pooledResource) {
            destroy(pooledResource);
        }
        ensureIdle(1, false);
    }

    //

    private void addIdleResource(final PooledResource<T> resource) throws Exception {
        if (resource != null) {
            factory.deactivateObject(resource);
            idleObjects.addFirst(resource);
        }
    }


    private PooledResource<T> create() throws Exception {

        int localMaxTotal = getMaxTotal();
        // This simplifies the code later in this method
        if (localMaxTotal < 0) {
            localMaxTotal = Integer.MAX_VALUE;
        }

        final long localStartTimeMillis = System.currentTimeMillis();
        final long localMaxWaitTimeMillis = Math.max(getMaxWaitMillis(), 0);

        // Flag that indicates if create should:
        // - TRUE:  call the factory to create an object
        // - FALSE: return null
        // - null:  loop and re-test the condition that determines whether to
        //          call the factory
        Boolean create = null;
        while (create == null) {
            synchronized (createResourceCountLock) {
                final long newCreateCount = createCount.incrementAndGet();
                if (newCreateCount > localMaxTotal) {
                    // The pool is currently at capacity or in the process of
                    // making enough new objects to take it to capacity.
                    createCount.decrementAndGet();
                    if (createResourceCount == 0) {
                        // There are no createReso() calls in progress so the
                        // pool is at capacity. Do not attempt to create a new
                        // object. Return and wait for an object to be returned
                        create = Boolean.FALSE;
                    } else {
                        // There are makeObject() calls in progress that might
                        // bring the pool to capacity. Those calls might also
                        // fail so wait until they complete and then re-test if
                        // the pool is at capacity or not.
                        createResourceCountLock.wait(localMaxWaitTimeMillis);
                    }
                } else {
                    // The pool is not at capacity. Create a new object.
                    createResourceCount++;
                    create = Boolean.TRUE;
                }
            }

            // Do not block more if maxWaitTimeMillis is set.
            if (create == null &&
                    (localMaxWaitTimeMillis > 0
                            && System.currentTimeMillis() - localStartTimeMillis >= localMaxWaitTimeMillis)) {
                create = Boolean.FALSE;
            }
        }

        if (!create.booleanValue()) {
            return null;
        }

        final PooledResource<T> pooledResource;
        try {
            pooledResource = factory.createObject();
        } catch (final Throwable e) {
            createCount.decrementAndGet();
            throw e;
        } finally {
            synchronized (createResourceCountLock) {
                createResourceCount--;
                createResourceCountLock.notifyAll();
            }
        }

        createCount.incrementAndGet();
        allObjects.put(new IdentityWrapper<>(pooledResource.getResource()), pooledResource);
        return pooledResource;
    }

    private void destroy(final PooledResource<T> toDestroy) throws Exception {
        idleObjects.remove(toDestroy);
        allObjects.remove(new IdentityWrapper<>(toDestroy.getResource()));
        try {
            factory.destroyObject(toDestroy);
        } finally {
            destroyedCount.incrementAndGet();
            createCount.decrementAndGet();
        }
    }


    private void ensureIdle(final int idleCount, final boolean always) throws Exception {
        // TODO: impelment isClosed functionality
        //if (idleCount < 1 || isClosed() || (!always && !idleObjects.hasTakeWaiters())) {
        //    return;
        //}

        while (idleObjects.size() < idleCount) {
            final PooledResource<T> p = create();
            if (p == null) {
                // Can't create objects, no reason to think another call to
                // create will work. Give up.
                break;
            }
            idleObjects.addFirst(p);
        }
        //if (isClosed()) {
            // Pool closed while object was being added to idle objects.
            // Make sure the returned object is destroyed rather than left
            // in the idle object pool (which would effectively be a leak)
            //clear();
        //}
    }

    // Inner classes

    /**
     * Wrapper for objects under management by the pool.
     * <p>
     * GenericObjectPool and GenericKeyedObjectPool maintain references to all
     * objects under management using maps keyed on the objects. This wrapper
     * class ensures that objects can work as hash keys.
     *
     * @param <T> type of objects in the pool
     */
    static class IdentityWrapper<T> {

        private final T instance;

        public IdentityWrapper(final T instance) {
            this.instance = instance;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(instance);
        }

        @Override
        @SuppressWarnings("rawtypes")
        public boolean equals(final Object other) {
            return other instanceof IdentityWrapper &&
                    ((IdentityWrapper) other).instance == instance;
        }

        public T getObject() {
            return instance;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("IdentityWrapper [instance=");
            builder.append(instance);
            builder.append("]");
            return builder.toString();
        }
    }

}
