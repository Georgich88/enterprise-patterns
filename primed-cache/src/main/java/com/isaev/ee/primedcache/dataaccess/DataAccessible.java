package com.isaev.ee.primedcache.dataaccess;

import com.isaev.ee.primedcache.key.Key;

import java.util.Optional;

public interface DataAccessible<T,S,D> {

    Optional<D> read(Key<T, S> specificKey);

}
