package com.isaev.ee.cacheaccessor.cache;

import com.isaev.ee.cacheaccessor.dataaccess.DataAccessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CacheAccessorTest {

    @Test
    void shouldCacheFirstPerson(){
        CacheAccessor accessor = new CacheAccessor(new DataAccessor());
        assertTrue(accessor.getFirstPeopleCache().isEmpty());
        assertDoesNotThrow(() -> accessor.findFirst());
        assertFalse(accessor.getFirstPeopleCache().isEmpty());
    }

    @Test
    void shouldCacheLastPerson(){
        CacheAccessor accessor = new CacheAccessor(new DataAccessor());
        assertTrue(accessor.getLastPeopleCache().isEmpty());
        assertDoesNotThrow(() -> accessor.findLast());
        assertFalse(accessor.getLastPeopleCache().isEmpty());
    }

}