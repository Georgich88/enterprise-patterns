package com.isaev.ee.cacheaccessor.cache;

import com.isaev.ee.cacheaccessor.dataaccess.DataAccessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CashAccessorTest {

    @Test
    void shouldCacheFirstPerson(){
        CashAccessor accessor = new CashAccessor(new DataAccessor());
        assertTrue(accessor.getFirstPeopleCache().isEmpty());
        assertDoesNotThrow(() -> accessor.findFirst());
        assertFalse(accessor.getFirstPeopleCache().isEmpty());
    }

    @Test
    void shouldCacheLastPerson(){
        CashAccessor accessor = new CashAccessor(new DataAccessor());
        assertTrue(accessor.getLastPeopleCache().isEmpty());
        assertDoesNotThrow(() -> accessor.findLast());
        assertFalse(accessor.getLastPeopleCache().isEmpty());
    }

}