package com.isaev.ee.serializedlob.database.utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

public class QueryExecutor {

    private static final String PATH_TO_CREATE_USER_QUERY = "/create-user.sql";
    private static final String PATH_TO_CREATE_DB_QUERY = "/create-db-serialized-lob.sql";
    private static final String PATH_TO_DROP_DB_QUERY = "/drop-db-serialized-lob.sql";
    private static final String PATH_TO_CREATE_TABLE_PERSONS_QUERY = "/create-table-customers.sql";

    private static final String UTF8_ENCODING = "UTF-8";

    private final static Logger logger = Logger.getLogger(QueryExecutor.class);

    public static final String MESSAGE_ERROR_CANNOT_CREATE_USER = "Cannot create table database user";
    public static final String MESSAGE_ERROR_CANNOT_EXECUTE_QUERY = "Cannot execute query";
    public static final String MESSAGE_ERROR_CANNOT_READ_THE_QUERY_FILE = "Cannot read the query text file: %s";

    public static void createDatabase() {

        try {
            executeQuery(computeQueryText(PATH_TO_CREATE_DB_QUERY));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteDatabase() {

        try {
            executeQuery(computeQueryText(PATH_TO_DROP_DB_QUERY));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createUser() {

        try {
            executeQuery(computeQueryText(PATH_TO_CREATE_USER_QUERY));
        } catch (Exception e) {
            logger.error(MESSAGE_ERROR_CANNOT_CREATE_USER, e);
        }
    }

    public static void createTables() {
        executeQuery(computeQueryText(PATH_TO_CREATE_TABLE_PERSONS_QUERY));
    }

    private static void executeQuery(String queryText) {

        try (Connection connection = ConnectionUtils.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(queryText);
        } catch (Exception e) {
            logger.error(MESSAGE_ERROR_CANNOT_EXECUTE_QUERY, e);
        }
    }

    private static String computeQueryText(String filePath) {

        try (FileInputStream inputFile = new FileInputStream(QueryExecutor.class.getResource(filePath).getFile())) {
            return computeQueryFileContent(inputFile);
        } catch (IOException e) {
            logger.error(String.format(MESSAGE_ERROR_CANNOT_READ_THE_QUERY_FILE, filePath), e);
        }

        return "";
    }

    private static String computeQueryFileContent(FileInputStream inputStream) {

        String line;

        try (InputStreamReader streamReader = new InputStreamReader(inputStream, QueryExecutor.UTF8_ENCODING);
             BufferedReader reader = new BufferedReader(streamReader)) {
            StringBuilder contentBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line);
                contentBuilder.append('\n');
            }
            return contentBuilder.toString();
        } catch (IOException e) {
            logger.error(MESSAGE_ERROR_CANNOT_READ_THE_QUERY_FILE, e);
        }

        return "";

    }


}
