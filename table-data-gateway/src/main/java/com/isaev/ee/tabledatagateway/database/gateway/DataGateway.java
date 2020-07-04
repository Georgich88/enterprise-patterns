package com.isaev.ee.tabledatagateway.database.gateway;

import com.isaev.ee.tabledatagateway.database.exception.DataGatewayException;

import java.util.Optional;

public interface DataGateway<T, U> {

    void save(T data) throws DataGatewayException;
    Optional<T> findById(U id) throws DataGatewayException;
    void update(T data) throws DataGatewayException;
    void delete(T data) throws DataGatewayException;

}
