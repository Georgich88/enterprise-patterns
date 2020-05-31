package com.isaev.ee.tablemodule.database.jdbc;

import com.isaev.ee.tablemodule.database.utils.ConnectionUtils;
import com.isaev.ee.tablemodule.tables.Product;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class ProductJdbcGateway {
    private static final String SELECTION_BY_ID_QUERY_TEMPLATE =
            "SELECT products.product_id, " +
            "products.name, " +
            "products.type " +
            "FROM products " +
            "WHERE products.product_id = ?";
    private static final int SELECTION_BY_ID_PARAMETER = 1;
    private static final String NAME_FIELD_NAME = "name";
    private static final String TYPE_FIELD_NAME = "type";
    private static final String MESSAGE_TEMPLATE_CANNOT_FIND_PRODUCT = "Cannot find contract by ID: %s";

    private static final String INSERTION_QUERY_TEMPLATE = "INSERT INTO products (product_id, name, type) VALUES (?, ?, ?)";
    private static final int INSERTION_PRODUCT_ID_PARAMETER = 1;
    private static final int INSERTION_NAME_PARAMETER = 2;
    private static final int INSERTION_TYPE_PARAMETER = 3;
    public static final String MESSAGE_TEMPLATE_CANNOT_SAVE_PRODUCT = "Cannot save product with ID: %s";

    private final static Logger logger = Logger.getLogger(ProductJdbcGateway.class);

    public void save(Product product) {
        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERTION_QUERY_TEMPLATE);) {
            statement.setInt(INSERTION_PRODUCT_ID_PARAMETER, product.getId());
            statement.setString(INSERTION_NAME_PARAMETER, product.getName());
            statement.setString(INSERTION_TYPE_PARAMETER, product.getType());
            statement.addBatch();
            statement.executeBatch();
        } catch (SQLException e) {
            logger.error(String.format(MESSAGE_TEMPLATE_CANNOT_SAVE_PRODUCT, product.getId()), e);
        }
    }

    public Optional<Product> findByProductId(int productId) {

        Optional<Product> product = Optional.empty();

        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECTION_BY_ID_QUERY_TEMPLATE)) {

            statement.setInt(SELECTION_BY_ID_PARAMETER, productId);

            var resultSet = statement.executeQuery();
            resultSet.next();

            String name = resultSet.getString(NAME_FIELD_NAME);
            String type = resultSet.getString(TYPE_FIELD_NAME);

            product = Optional.of(new Product(productId, name, type));

        } catch (SQLException e) {
            logger.error(String.format(MESSAGE_TEMPLATE_CANNOT_FIND_PRODUCT, productId), e);
        }

        return product;
    }

}

