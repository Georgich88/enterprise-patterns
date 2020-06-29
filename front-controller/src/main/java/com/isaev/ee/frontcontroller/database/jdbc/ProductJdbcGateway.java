package com.isaev.ee.frontcontroller.database.jdbc;

import com.isaev.ee.frontcontroller.database.utils.ConnectionUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductJdbcGateway {
    private static final String INSERTION_QUERY_TEMPLATE = "INSERT INTO products (product_id, name, type) VALUES (?, ?, ?)";
    private static final int INSERTION_PRODUCT_ID_PARAMETER = 1;
    private static final int INSERTION_NAME_PARAMETER = 2;
    private static final int INSERTION_TYPE_PARAMETER = 3;
    public static final String MESSAGE_TEMPLATE_CANNOT_SAVE_PRODUCT = "Cannot save product with ID: %s";

    private final static Logger logger = Logger.getLogger(ProductJdbcGateway.class);

    public void save(int productId, String name, String type) {
        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERTION_QUERY_TEMPLATE);) {
            statement.setInt(INSERTION_PRODUCT_ID_PARAMETER, productId);
            statement.setString(INSERTION_NAME_PARAMETER, name);
            statement.setString(INSERTION_TYPE_PARAMETER, type);
            statement.addBatch();
            statement.executeBatch();
        } catch (SQLException e) {
            logger.error(String.format(MESSAGE_TEMPLATE_CANNOT_SAVE_PRODUCT, productId), e);
        }
    }


}

