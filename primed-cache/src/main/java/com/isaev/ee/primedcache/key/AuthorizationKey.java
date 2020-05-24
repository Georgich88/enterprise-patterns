package com.isaev.ee.primedcache.key;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AuthorizationKey implements Key<String, String> {

    final Map<String, String> keys = new ConcurrentHashMap<>();

    @Override
    public synchronized boolean isPartialOf(Key<String, String> specificKey) {
        var specificKeys = specificKey.getKeys();
        for (var entry : keys.entrySet()) {
            if (!specificKeys.containsKey(entry.getKey())
                    || !specificKeys.get(entry.getKey()).contains(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Map<String, String> getKeys() {
        return Collections.unmodifiableMap(this.keys);
    }

    @Override
    public void addKey(String attribute, String value) {
        keys.put(attribute, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorizationKey that = (AuthorizationKey) o;
        return Objects.equals(keys, that.keys);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keys);
    }
}
