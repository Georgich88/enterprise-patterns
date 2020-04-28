package com.isaev.ee.datamapper.database.datamapper;

import com.isaev.ee.datamapper.database.exception.DataMapperException;

public interface DataMapper<T> {

    void update(T t) throws DataMapperException;
    void insert(T t) throws DataMapperException;
    void delete(T t) throws DataMapperException;

}
