package com.isaev.ee.connectionpool.pool;

public enum PooledResourceState {

    /**
     * Not in use
     */
    IDLE,
    /**
     * In use
     */
    ALLOCATED,
    /**
     * Ready to be released
     */
    RELEASE

}
