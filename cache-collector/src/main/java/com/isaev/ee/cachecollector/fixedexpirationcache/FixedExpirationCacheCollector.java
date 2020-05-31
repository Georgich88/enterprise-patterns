package com.isaev.ee.cachecollector.fixedexpirationcache;

import com.isaev.ee.cachecollector.cache.CacheCollector;
import com.isaev.ee.cachecollector.cache.CacheEntry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FixedExpirationCacheCollector<K, V> implements CacheCollector<K, V> {

    public static final String MESSAGE_ENTRY_WRONG_ENTRY_TYPE = "Entry should be FixedExpirationCacheEntry type";
    private Map<K, FixedExpirationCacheEntry<V>> cache;
    private Queue<K> creationQueue;
    private final Duration interval;

    // Constructor

    public FixedExpirationCacheCollector(long interval) {
        this.cache = new ConcurrentHashMap<>();
        this.creationQueue = new ConcurrentLinkedQueue<>();
        this.interval = Duration.ofSeconds(interval);
    }

    @Override
    public void collect() {

        LocalDateTime expirationTime = LocalDateTime.now().minus(interval);

        synchronized (this) {
            List<K> toRemove = new ArrayList<>();
            for (K key : creationQueue) {
                FixedExpirationCacheEntry entry = cache.get(key);
                if (entry.getCreationTime().isBefore(expirationTime)) {
                    toRemove.add(key);
                } else {
                    break;
                }
            }
            for (K key : toRemove) {
                creationQueue.remove(key);
                cache.remove(key);
            }
        }

    }

    @Override
    public void add(K key, CacheEntry<V> entry) throws IllegalAccessException {
        if (!(entry instanceof FixedExpirationCacheEntry)) {
            throw new IllegalAccessException(MESSAGE_ENTRY_WRONG_ENTRY_TYPE);
        }
        add(key, (FixedExpirationCacheEntry) entry);
    }

    public void add(K key, FixedExpirationCacheEntry<V> entry) {
        cache.put(key, entry);
        creationQueue.offer(key);
    }

    // Getters

    public Map<K, CacheEntry<V>> getCache() {
        synchronized (this) {
            return Collections.unmodifiableMap(cache);
        }
    }

}
