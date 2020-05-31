package com.isaev.ee.cachecollector.cache;

import java.util.Map;

public interface CacheEntryFactory<K, V> {
    CacheEntry<V> createCacheEntry(K key, V value) throws Exception;
    Map<K, CacheEntry<V>> getCache();
}
