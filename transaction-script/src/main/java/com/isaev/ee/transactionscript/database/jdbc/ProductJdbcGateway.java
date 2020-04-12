package com.isaev.ee.transactionscript.database.jdbc;

import com.isaev.ee.transactionscript.database.utils.ConnectionUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductJdbcGateway {

    private static final String INSERTION_QUERY_TEMPLATE = "INSERT INTO products (product_id, name, type) VALUES (?, ?, ?)";
    private static final int INSERTION_PRODUCT_ID_PARAMETER = 1;
    private static final int INSERTION_NAME_PARAMETER = 2;
    private static final int INSERTION_TYPE_PARAMETER = 3;
    private static final String SELECTION_BY_ID_QUERY_TEMPLATE =
            "SELECT products.product_id, " +
                    "products.name, " +
                    "products.type " +
                    "FROM products " +
                    "WHERE products.product_id = ?";
    private static final int SELECTION_BY_ID_PRODUCT_ID_PARAMETER = 1;

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
            logger.error("Cannot save products", e);
        }
    }

    public ResultSet findById(int productId) {

        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECTION_BY_ID_QUERY_TEMPLATE)) {
            statement.setInt(SELECTION_BY_ID_PRODUCT_ID_PARAMETER, productId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            logger.error("Cannot find product", e);
        }

        return null;
    }

}

