package com.isaev.ee.primedcache.cache;

import com.isaev.ee.primedcache.dataaccess.DataAccessible;
import com.isaev.ee.primedcache.domain.AuthorizationData;
import com.isaev.ee.primedcache.key.Key;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CacheAccessor implements DataAccessible<String, String, AuthorizationData> {

    private final DataAccessible<String, String, AuthorizationData> dataAccessor;
    private final Map<Key<String, String>, AuthorizationData> localCache = new ConcurrentHashMap<>();

    public CacheAccessor(DataAccessible<String, String, AuthorizationData> dataAccessor) {
        this.dataAccessor = dataAccessor;
    }

    public void prepopulate(Key<String, String> specificKey) {
        localCache.putIfAbsent(specificKey, this.dataAccessor.read(specificKey).orElseThrow());
    }

    @Override
    public Optional<AuthorizationData> read(Key<String, String> specificKey) {
        if (localCache.containsKey(specificKey)) {
            return Optional.of(localCache.get(specificKey));
        }
        for (var entry : this.localCache.entrySet()){
            if (entry.getKey().isPartialOf(specificKey)){
                return Optional.of(entry.getValue());
            }
        }
        return this.dataAccessor.read(specificKey);
    }

    public Map<Key<String, String>, AuthorizationData> getLocalCache() {
        return Collections.unmodifiableMap(localCache);
    }
}
