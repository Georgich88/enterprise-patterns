package com.isaev.ee.cacheaccessor.dataaccess;

import java.util.Optional;

public interface DataAccessible<T> {

    Optional<T> findFirst();
    Optional<T> findLast();

}
