package com.isaev.ee.cachecollector.fixedexpirationcache;

import com.isaev.ee.cachecollector.cache.CacheCollector;
import com.isaev.ee.cachecollector.cache.CacheEntry;
import com.isaev.ee.cachecollector.cache.CacheEntryFactory;

import java.util.Map;

public class FixedExpirationCacheEntryFactory<K, V> implements CacheEntryFactory<K, V> {

    private CacheCollector<K, V> collector;

    public FixedExpirationCacheEntryFactory(long expirationInterval) {
        this.collector = new FixedExpirationCacheCollector<>(expirationInterval);
    }

    @Override
    public CacheEntry<V> createCacheEntry(K key, V value) throws Exception {
        synchronized (this) {
            var entry = new FixedExpirationCacheEntry<V>(value);
            collector.add(key, entry);
            return entry;
        }
    }

    public void collect(){
        this.collector.collect();
    }

    public Map<K, CacheEntry<V>> getCache(){
        return this.collector.getCache();
    }


}
