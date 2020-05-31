package com.isaev.ee.cachecollector.cache;

import java.util.Optional;

public interface CacheEntry<T> {
    Optional<T> getData();
}
