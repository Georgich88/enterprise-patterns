package com.isaev.ee.lazyload.database.datamapper;

import com.isaev.ee.lazyload.database.exception.DataMapperException;

public interface DataMapper<T> {

    void update(T t) throws DataMapperException;
    void insert(T t) throws DataMapperException;
    void delete(T t) throws DataMapperException;

}
