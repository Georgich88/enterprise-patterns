package com.isaev.ee.connectionpool.connectionpool;


/**
 * Simple structure that encapsulates the configuration settings for {@link ConnectionPool}.
 *
 * @param <T> type of connection.
 */
public class ConnectionPoolConfig<T> {

    /**
     * The default value for the {@code maxTotal} configuration attribute.
     */
    public static final int DEFAULT_MAX_TOTAL = 8;

    /**
     * The default value for the {@code maxIdle} configuration attribute.
     */
    public static final int DEFAULT_MAX_IDLE = 8;

    /**
     * The default value for the {@code minIdle} configuration attribute.
     */
    public static final int DEFAULT_MIN_IDLE = 0;

    /**
     * The default value for the {@code maxWait} configuration attribute.
     */
    public static final long DEFAULT_MAX_WAIT_MILLIS = -1L;

    private int maxTotal = DEFAULT_MAX_TOTAL;

    private int maxIdle = DEFAULT_MAX_IDLE;

    private int minIdle = DEFAULT_MIN_IDLE;

    /**
     * Get the value for the {@code maxTotal} configuration attribute
     * for pools created with this configuration instance.
     *
     * @return The current setting of {@code maxTotal} for this
     * configuration instance
     */
    public int getMaxTotal() {
        return maxTotal;
    }

    /**
     * Set the value for the {@code maxTotal} configuration attribute for
     * pools created with this configuration instance.
     *
     * @param maxTotal The new setting of {@code maxTotal}
     *                 for this configuration instance
     */
    public void setMaxTotal(final int maxTotal) {
        this.maxTotal = maxTotal;
    }


    /**
     * Get the value for the {@code maxIdle} configuration attribute
     * for pools created with this configuration instance.
     *
     * @return The current setting of {@code maxIdle} for this
     * configuration instance
     */
    public int getMaxIdle() {
        return maxIdle;
    }

    /**
     * Set the value for the {@code maxIdle} configuration attribute for
     * pools created with this configuration instance.
     *
     * @param maxIdle The new setting of {@code maxIdle}
     *                for this configuration instance
     */
    public void setMaxIdle(final int maxIdle) {
        this.maxIdle = maxIdle;
    }


    /**
     * Get the value for the {@code minIdle} configuration attribute
     * for pools created with this configuration instance.
     *
     * @return The current setting of {@code minIdle} for this
     * configuration instance
     */
    public int getMinIdle() {
        return minIdle;
    }

    /**
     * Set the value for the {@code minIdle} configuration attribute for
     * pools created with this configuration instance.
     *
     * @param minIdle The new setting of {@code minIdle}
     *                for this configuration instance
     */
    public void setMinIdle(final int minIdle) {
        this.minIdle = minIdle;
    }

}
