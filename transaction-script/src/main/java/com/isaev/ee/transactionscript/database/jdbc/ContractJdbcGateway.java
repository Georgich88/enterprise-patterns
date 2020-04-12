package com.isaev.ee.transactionscript.database.jdbc;

import com.isaev.ee.transactionscript.database.utils.ConnectionUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class ContractJdbcGateway {

    private static final String INSERTION_QUERY_TEMPLATE = "INSERT INTO contracts (contract_id, product_id, date_signed, revenue) VALUES (?, ?, ?, ?)";
    private static final int INSERTION_CONTRACT_ID_PARAMETER = 1;
    private static final int INSERTION_PRODUCT_ID_PARAMETER = 2;
    private static final int INSERTION_DATE_SIGNED_PARAMETER = 3;
    private static final int INSERTION_REVENUE_PARAMETER = 4;

    private static final String SELECTION_BY_ID_QUERY_TEMPLATE =
            "SELECT contracts.contract_id, " +
                    "contracts.product_id, " +
                    "contracts.date_signed, " +
                    "contracts.revenue, " +
                    "products.product_id" +
                    "FROM contracts " +
                    "INNER JOIN products " +
                    "ON contracts.product_id = products.product_id " +
                    "WHERE contracts.contract_id = ?";
    private static final int SELECTION_BY_ID_CONTRACT_ID_PARAMETER = 1;


    private final static Logger logger = Logger.getLogger(ContractJdbcGateway.class);

    public void save(int contractId, int productId, LocalDate dateSigned, BigDecimal revenue) {

        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERTION_QUERY_TEMPLATE);) {
            statement.setInt(INSERTION_CONTRACT_ID_PARAMETER, contractId);
            statement.setInt(INSERTION_PRODUCT_ID_PARAMETER, productId);
            statement.setDate(INSERTION_DATE_SIGNED_PARAMETER, Date.valueOf(dateSigned));
            statement.setBigDecimal(INSERTION_REVENUE_PARAMETER, revenue);
            statement.addBatch();
            statement.executeBatch();
        } catch (SQLException e) {
            logger.error("Cannot save contracts", e);
        }
    }

    public ResultSet findById(int contractId) {

        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECTION_BY_ID_QUERY_TEMPLATE)) {
            statement.setInt(SELECTION_BY_ID_CONTRACT_ID_PARAMETER, contractId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            logger.error("Cannot find contract", e);
        }

        return null;
    }

}
