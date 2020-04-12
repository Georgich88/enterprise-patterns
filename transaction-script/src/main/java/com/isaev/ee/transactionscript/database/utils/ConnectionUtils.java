package com.isaev.ee.transactionscript.database.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.isaev.ee.transactionscript.database.exceptions.NoDatabasePropertiesFileException;
import org.apache.log4j.Logger;


public class ConnectionUtils {

    private static final String PROPERTIES_FILE_PATH = "src/main/resources/db.properties";
    private static final String URL_PROPERTY_NAME = "db.url";
    private static final String USER_PROPERTY_NAME = "db.user";
    private static final String PASSWORD_PROPERTY_NAME = "db.password";

    private final static Logger logger = Logger.getLogger(ConnectionUtils.class);

    public static Connection getConnection() throws SQLException, NoDatabasePropertiesFileException {
        return getConnection(PROPERTIES_FILE_PATH);
    }

    public static Connection getConnection(String propertiesFilePath) throws SQLException, NoDatabasePropertiesFileException {

        Properties properties = new Properties();

        try {
            InputStream inputProperties = new FileInputStream(propertiesFilePath);
            properties.load(inputProperties);

        } catch (IOException e) {
            logger.error("Cannot read properties file", e);
            throw new NoDatabasePropertiesFileException("Cannot read properties file", e);
        }

        Driver driver = new org.postgresql.Driver();
        DriverManager.deregisterDriver(driver);

        String url = properties.getProperty(URL_PROPERTY_NAME);
        String user = properties.getProperty(USER_PROPERTY_NAME);
        String password = properties.getProperty(PASSWORD_PROPERTY_NAME);

        return DriverManager.getConnection(url, user, password);

    }



}
