package com.isaev.ee.connectionpool.pool;

import java.util.NoSuchElementException;

/**
 * A simple pooling interface.
 *
 * @param <T> type of an element being pooled in the resource pool.
 */
public interface ResourcePool<T> {

    void addResource() throws Exception, IllegalAccessError, UnsupportedOperationException;

    default void addResources(int number) throws Exception {
        for (int i = 0; i < number; i++) {
            addResource();
        }
    }

    /**
     * Obtains an resource from the pool
     *
     * @return an resource from the pool
     * @throws Exception
     * @throws NoSuchElementException
     * @throws IllegalStateException
     */
    T borrowResource() throws Exception, NoSuchElementException, IllegalStateException;


    /**
     * Clears any resources that are idle in the pool,
     * releasing any associated resources (optional operation).
     *
     * @throws Exception
     * @throws UnsupportedOperationException
     */
    void clear() throws Exception, UnsupportedOperationException;

    /**
     * Closes the pool, and free any resources associated with it.
     */
    void close();

    /**
     * Returns the number of resources currently borrowed from the pool.
     * Returns a negative value if this information is not available.
     *
     * @return the number of resources currently borrowed from this pool.
     */
    int getActiveNumber();

    /**
     * Returns the number of resources currently idle in the pool.
     *
     * @return the number of resources currently idle in this pool.
     */
    int getIdleNumber();

    /**
     * Releases an resource from the pool
     *
     * @param resource
     * @throws Exception
     */
    void releaseResource(T resource) throws Exception;

    void returnResource(T resource) throws Exception;
}
