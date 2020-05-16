package com.isaev.ee.primedcache.key;

public interface KeyFactory<T,S,D> {
    Key<T,S> createKey(D domainObject);
}
