package com.isaev.ee.connectionpool.application;

import com.isaev.ee.connectionpool.connectionfactory.DriverManagerConnectionFactory;
import com.isaev.ee.connectionpool.connectionpool.ConnectionPool;
import com.isaev.ee.connectionpool.connectionpool.ConnectionPoolConfig;
import com.isaev.ee.connectionpool.pool.PooledResourceFactory;

import java.sql.Connection;

/**
 * Demo application demonstrates connection pool implementation.
 *
 * @author Georgy Isaev
 */
public class Application {

    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String JDBC_DB_URL = "jdbc:postgresql://localhost:5432/data_mapper";
    private static final String JDBC_USER = "data_mapper";
    private static final String JDBC_PASS = "123456";

    private static final int DELAY = 31*1000;

    private static ConnectionPool<Connection> connectionPool = null;

    public static void setupPool() throws Exception {

        Class.forName(JDBC_DRIVER);
        PooledResourceFactory factory = new DriverManagerConnectionFactory(JDBC_DB_URL, JDBC_USER, JDBC_PASS);
        ConnectionPoolConfig config = new ConnectionPoolConfig();
        config.setTimedConnectionPool(true);
        connectionPool = new ConnectionPool<Connection>(factory, config);
        connectionPool.setMaxTotal(10);

    }

    public static ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    private static void printDbStatus() {
        System.out.println("Max.: " + getConnectionPool().getMaxTotal()
                + "; Active: " + getConnectionPool().getActiveNumber()
                + "; Idle: " + getConnectionPool().getIdleNumber());
    }

    public static void main(String[] args) throws Exception {

        setupPool();
        printDbStatus();
        Connection connection = connectionPool.borrowResource();
        printDbStatus();
        connectionPool.returnResource(connection);
        printDbStatus();
        connectionPool.clear();
        printDbStatus();
        connectionPool.addResource();
        printDbStatus();
        Thread.sleep((long) (DELAY));
        printDbStatus();

    }
}
