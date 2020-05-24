package com.isaev.ee.primedcache.cache;

import com.isaev.ee.primedcache.dataaccess.DataAccessor;
import com.isaev.ee.primedcache.domain.AuthorizationData;
import com.isaev.ee.primedcache.key.AuthorizationKeyFactory;
import com.isaev.ee.primedcache.key.KeyFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CacheAccessorTest {

    final KeyFactory<String, String, AuthorizationData> factory = new AuthorizationKeyFactory();

    @Test
    void shouldPrepopulate() {
        CacheAccessor accessor = new CacheAccessor(new DataAccessor());
        assertTrue(accessor.getLocalCache().isEmpty());
        assertDoesNotThrow(() -> {
            var chandlerKey = factory.createKey(new AuthorizationData("Chandler", "Support"));
            accessor.prepopulate(chandlerKey);
        });
        assertFalse(accessor.getLocalCache().isEmpty());
    }

    @Test
    void shouldRead() {
        CacheAccessor accessor = new CacheAccessor(new DataAccessor());
        assertTrue(accessor.getLocalCache().isEmpty());
        assertDoesNotThrow(() -> {
            var chandlerKey = factory.createKey(new AuthorizationData("Chandler", "Support"));
            chandlerKey.addKey("Country", "Ukraine");
            assertTrue(accessor.read(chandlerKey).isPresent());
        });
    }


}