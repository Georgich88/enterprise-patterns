package com.isaev.ee.connectionpool.connectionfactory;

import com.isaev.ee.connectionpool.pool.PooledResource;
import com.isaev.ee.connectionpool.pool.PooledResourceFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DriverManagerConnectionFactory implements PooledResourceFactory<Connection> {

    static {
        DriverManager.getDrivers();
    }


    public DriverManagerConnectionFactory(final String connectionUri) {
        this.connectionUri = connectionUri;
        this.properties = new Properties();
    }

    public DriverManagerConnectionFactory(final String connectionUri, final Properties properties) {
        this.connectionUri = connectionUri;
        this.properties = properties;
    }

    public DriverManagerConnectionFactory(final String connectionUri, final String userName, final String userPassword) {
        this.connectionUri = connectionUri;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    @Override
    public PooledResource<Connection> createObject() throws SQLException {

        final Connection connection;

        if (null == properties && userName == null && userPassword == null) {
            connection = DriverManager.getConnection(connectionUri);
        } else if (null == properties) {
            connection = DriverManager.getConnection(connectionUri, userName, userPassword);
        } else {
            connection = DriverManager.getConnection(connectionUri, properties);
        }

        return new PooledConnection(connection);
    }

    @Override
    public void destroyObject(PooledResource<Connection> p) throws Exception {

    }

    @Override
    public void activateObject(PooledResource<Connection> p) throws Exception {

    }

    @Override
    public void deactivateObject(PooledResource<Connection> p) throws Exception {

    }

    private final String connectionUri;
    private String userName;
    private String userPassword;
    private Properties properties;
}
