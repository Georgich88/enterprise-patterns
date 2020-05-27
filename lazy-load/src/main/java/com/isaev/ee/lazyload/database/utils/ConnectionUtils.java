package com.isaev.ee.lazyload.database.utils;

import com.isaev.ee.lazyload.database.exception.NoDatabasePropertiesFileException;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class ConnectionUtils {

    public static final String MESSAGE_CANNOT_READ_PROPERTIES_FILE = "Cannot read properties file";
    private static final String PROPERTIES_FILE_PATH = "/db.properties";
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
            InputStream inputProperties = new FileInputStream(ConnectionUtils.class.getResource(propertiesFilePath).getFile());
            properties.load(inputProperties);

        } catch (IOException e) {
            logger.error(MESSAGE_CANNOT_READ_PROPERTIES_FILE, e);
            throw new NoDatabasePropertiesFileException(MESSAGE_CANNOT_READ_PROPERTIES_FILE, e);
        }
        Driver driver = new org.postgresql.Driver();
        DriverManager.deregisterDriver(driver);
        String url = properties.getProperty(URL_PROPERTY_NAME);
        String user = properties.getProperty(USER_PROPERTY_NAME);
        String password = properties.getProperty(PASSWORD_PROPERTY_NAME);
        return DriverManager.getConnection(url, user, password);

    }




}
