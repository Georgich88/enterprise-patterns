package com.isaev.ee.connectionpool.connectionpool;

import com.isaev.ee.connectionpool.connectionfactory.DriverManagerConnectionFactory;
import com.isaev.ee.connectionpool.pool.PooledResourceFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionPoolTest {

    private static final String JDBC_PROPERTY_DRIVER = "org.postgresql.Driver";
    private static final String JDBC_PROPERTY_DB_URL = "jdbc:postgresql://localhost:5432/data_mapper";
    private static final String JDBC_PROPERTY_USER = "data_mapper";
    private static final String JDBC_PROPERTY_PASS = "123456";

    private ConnectionPool<Connection> connectionPool = null;

    @BeforeEach
    void setupPool() throws Exception {

        Class.forName(JDBC_PROPERTY_DRIVER);
        PooledResourceFactory factory = new DriverManagerConnectionFactory(
                JDBC_PROPERTY_DB_URL, JDBC_PROPERTY_USER, JDBC_PROPERTY_PASS);
        connectionPool = new ConnectionPool<Connection>(factory);
        connectionPool.setMaxTotal(10);

    }

    @AfterEach
    void closePool() throws Exception {
        connectionPool.close();
    }

    @Test
    void shouldAddResource() {
        assertDoesNotThrow(() -> connectionPool.addResource());
        assertDoesNotThrow(() -> connectionPool.addResource());
        assertEquals(2, connectionPool.getIdleNumber());
    }

    @Test
    void shouldBorrowResource() {
        assertDoesNotThrow(() -> connectionPool.borrowResource());
        assertDoesNotThrow(() -> connectionPool.borrowResource());
        assertEquals(2, connectionPool.getActiveNumber());
    }

    @Test
    void shouldClear() {
        assertDoesNotThrow(() -> connectionPool.addResource());
        assertDoesNotThrow(() -> connectionPool.borrowResource());
        assertDoesNotThrow(() -> connectionPool.addResource());
        assertEquals(1, connectionPool.getActiveNumber());
        assertEquals(1, connectionPool.getIdleNumber());
        assertDoesNotThrow(() -> connectionPool.clear());
        assertEquals(1, connectionPool.getActiveNumber());
        assertEquals(0, connectionPool.getIdleNumber());
    }

    @Test
    void shouldClose() {
        assertDoesNotThrow(() -> connectionPool.addResource());
        assertDoesNotThrow(() -> connectionPool.close());
        assertTrue(connectionPool.isClosed());
    }

    @Test
    void shouldGetActiveNumber() {
        assertDoesNotThrow(() -> connectionPool.addResource());
        assertDoesNotThrow(() -> connectionPool.borrowResource());
        assertDoesNotThrow(() -> connectionPool.addResource());
        assertEquals(1, connectionPool.getActiveNumber());
    }

    @Test
    void shouldGetIdleNumber() {
        assertDoesNotThrow(() -> connectionPool.addResource());
        assertEquals(1, connectionPool.getIdleNumber());
    }

    @Test
    void shouldReturnResource() throws Exception {
        Connection connection = connectionPool.borrowResource();
        System.out.println(connection.getClientInfo());
        assertEquals(1, connectionPool.getActiveNumber());
        connectionPool.returnResource(connection);
        assertEquals(0, connectionPool.getActiveNumber());
        assertEquals(1, connectionPool.getIdleNumber());
    }
}