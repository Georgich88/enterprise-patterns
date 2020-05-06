package com.isaev.ee.connectionpool.connectionpool;

import com.isaev.ee.connectionpool.pool.PooledResource;
import com.isaev.ee.connectionpool.pool.PooledResourceFactory;
import com.isaev.ee.connectionpool.pool.PooledResourceState;
import com.isaev.ee.connectionpool.pool.ResourcePool;
import com.isaev.ee.connectionpool.timer.TimedResource;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple connection pool implementation
 *
 * @param <T> type of connection in the pool
 */
public class ConnectionPool<T> implements ResourcePool<T> {

    // Message constants

    public static final String MESSAGE_FACTORY_IS_NOT_INITIALIZED = "Cannot add resource. Factory is not initialized.";
    public static final String MESSAGE_ERROR_UNABLE_TO_ACTIVATE_RESOURCE = "Unable to activate resource";
    public static final String MESSAGE_ERROR_TIMEOUT_WAITING_IDLE = "Timeout waiting for idle resource";
    public static final String MESSAGE_ERROR_RETURNED_RESOURCE_NOT_IN_POOL = "Returned resource not currently part of this pool";

    // Constructors

    public ConnectionPool(final PooledResourceFactory<T> factory) {
        this(factory, new ConnectionPoolConfig<T>());
    }

    public ConnectionPool(final PooledResourceFactory<T> factory, final ConnectionPoolConfig<T> config) {

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
    private volatile int idleDuration = ConnectionPoolConfig.DEFAULT_IDLE_DURATION;
    private final PooledResourceFactory<T> factory;
    private volatile boolean timedConnectionPool = false;

    // Internal attributes

    private final Map<IdentityWrapper<T>, PooledResource<T>> allObjects = new ConcurrentHashMap<>();
    private final AtomicLong createCount = new AtomicLong(0);
    private final AtomicLong destroyedCount = new AtomicLong(0);
    private long createResourceCount = 0;
    private final Object createResourceCountLock = new Object();
    private volatile boolean closed = false;
    private final Object closeLock = new Object();
    private final LinkedBlockingDeque<PooledResource<T>> idleObjects;

    private final static Logger logger = Logger.getLogger(ConnectionPool.class);

    // Resource pool methods

    @Override
    public void addResource() throws Exception, IllegalAccessError {
        if (factory == null) {
            logger.error(MESSAGE_FACTORY_IS_NOT_INITIALIZED);
            throw new IllegalArgumentException(MESSAGE_FACTORY_IS_NOT_INITIALIZED);
        }
        PooledResource<T> resource = create();
        addIdleResource(resource);
    }

    @Override
    public T borrowResource() throws Exception {
        return borrowResource(getMaxWaitMillis());
    }

    @Override
    public void clear() throws Exception {

        PooledResource<T> resource = idleObjects.poll();

        while (resource != null) {
            try {
                destroy(resource);
            } catch (final Exception e) {
                logger.error(e);
            }
            resource = idleObjects.poll();
        }
    }

    @Override
    public void close() throws Exception {
        if (isClosed()) return;
        synchronized (closeLock) {
            if (isClosed()) {
                return;
            }
            closed = true;
            clear();
        }
    }

    @Override
    public int getActiveNumber() {
        return allObjects.size() - idleObjects.size();
    }

    @Override
    public int getIdleNumber() {
        return idleObjects.size();
    }

    @Override
    public void returnResource(T resource) throws Exception {

        final PooledResource<T> pooledResource = allObjects.get(new IdentityWrapper<>(resource));
        if (pooledResource == null) {
            throw new IllegalStateException(MESSAGE_ERROR_RETURNED_RESOURCE_NOT_IN_POOL);
        }
        final int maxIdleSave = getMaxIdle();
        if (isClosed() || maxIdleSave > -1 && maxIdleSave <= idleObjects.size()) {
            try {
                destroy(pooledResource);
            } catch (final Exception e) {
                logger.error(e);
            }
            try {
                ensureIdle(1, false);
            } catch (final Exception e) {
                logger.error(e);
            }
        } else {
            idleObjects.addFirst(pooledResource);
        }

        if (isClosed()) {
            clear();
        }
    }

    // Connection pool specific methods

    public void releaseResource(final PooledResource<T> resource) throws Exception {
        if (isClosed()) return;
        if (resource != null && resource.getState() == PooledResourceState.IDLE) {
            destroy(resource);
        }
    }

    public final boolean isClosed() {
        return closed;
    }

    private void addIdleResource(final PooledResource<T> resource) throws Exception {
        if (resource != null) {
            factory.deactivateObject(resource);
            idleObjects.addFirst(resource);
        }
    }

    private PooledResource<T> create() throws Exception {

        int localMaxTotal = getMaxTotal();

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
                    createCount.decrementAndGet();
                    if (createResourceCount == 0) {
                        create = Boolean.FALSE;
                    } else {
                        createResourceCountLock.wait(localMaxWaitTimeMillis);
                    }
                } else {
                    createResourceCount++;
                    create = Boolean.TRUE;
                }
            }

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
            if (isTimedConnectionPool()) {
                pooledResource = new TimedResource<>(factory.createObject(), this);
            } else {
                pooledResource = factory.createObject();
            }

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
        if (isClosed()) return;
        while (idleObjects.size() < idleCount) {
            final PooledResource<T> p = create();
            if (p == null) {
                break;
            }
            idleObjects.addFirst(p);
        }
        if (isClosed()) {
            // Pool closed while object was being added to idle objects.
            // Make sure the returned object is destroyed rather than left
            // in the idle object pool (which would effectively be a leak)
            clear();
        }
    }

    public T borrowResource(final long borrowMaxWaitMillis) throws Exception {

        PooledResource<T> resource = null;

        boolean create;

        while (resource == null) {
            create = false;
            resource = idleObjects.pollFirst();
            if (resource == null) {
                resource = create();
                if (resource != null) {
                    create = true;
                }
            }

            if (resource == null) {
                if (borrowMaxWaitMillis < 0) {
                    resource = idleObjects.takeFirst();
                } else {
                    resource = idleObjects.pollFirst(borrowMaxWaitMillis, TimeUnit.MILLISECONDS);
                }
            }
            if (resource == null) {
                throw new NoSuchElementException(MESSAGE_ERROR_TIMEOUT_WAITING_IDLE);
            }

            if (!resource.allocate()) {
                resource = null;
            }

            if (resource != null) {
                try {
                    factory.activateObject(resource);
                } catch (final Exception e) {
                    try {
                        destroy(resource);
                    } catch (final Exception e1) {
                    }
                    resource = null;
                    if (create) {
                        final NoSuchElementException exception = new NoSuchElementException(MESSAGE_ERROR_UNABLE_TO_ACTIVATE_RESOURCE);
                        exception.initCause(e);
                        throw exception;
                    }
                }
            }
        }

        return resource.getResource();
    }


    // Getters and setters

    public void setConfig(final ConnectionPoolConfig<T> config) {
        setMaxIdle(config.getMaxIdle());
        setMinIdle(config.getMinIdle());
        setMaxTotal(config.getMaxTotal());
        setIdleDuration(config.getIdleDuration());
        setTimedConnectionPool(config.isTimedConnectionPool());
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

    public int getIdleDuration() {
        return idleDuration;
    }

    public void setIdleDuration(int idleDuration) {
        this.idleDuration = idleDuration;
    }

    public boolean isTimedConnectionPool() {
        return timedConnectionPool;
    }

    public void setTimedConnectionPool(boolean timedConnectionPool) {
        this.timedConnectionPool = timedConnectionPool;
    }

    // Inner classes

    /**
     * Wrapper for resources under the management by the pool.
     *
     * @param <T> type of connection in the pool
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
