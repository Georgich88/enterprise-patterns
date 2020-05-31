package com.isaev.ee.cachecollector.fixedexpirationcache;

import com.isaev.ee.cachecollector.cache.CacheEntry;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.StringJoiner;

public class FixedExpirationCacheEntry<T> implements CacheEntry<T> {

    private T data;
    private LocalDateTime creationTime;

    // Constructors

    public FixedExpirationCacheEntry(T data) {
        this.data = data;
        this.creationTime = LocalDateTime.now();
    }

    // Getters

    @Override
    public Optional<T> getData() {
        return Optional.empty();
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    // Object inherited methods

    @Override
    public String toString() {
        return new StringJoiner(", ", FixedExpirationCacheEntry.class.getSimpleName() + "[", "]")
                .add("data=" + data)
                .add("creationTime=" + creationTime)
                .toString();
    }
}
