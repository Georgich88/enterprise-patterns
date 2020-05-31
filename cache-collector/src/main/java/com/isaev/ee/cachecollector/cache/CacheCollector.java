package com.isaev.ee.cachecollector.cache;

import java.util.Map;

public interface CacheCollector<K, V> {
    void collect();
    void add(K key, CacheEntry<V> entry) throws Exception;
    Map<K, CacheEntry<V>> getCache();
}
