package com.isaev.ee.transactionscript.database.jdbc;

import com.isaev.ee.transactionscript.database.utils.ConnectionUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class RevenueRecognitionJdbcGateway {

    private static final String INSERTION_QUERY_TEMPLATE = "INSERT INTO revenue_recognition (contract_id, date_recognition, amount) VALUES (?, ?, ?)";
    private static final int INSERTION_CONTRACT_ID_PARAMETER = 1;
    private static final int INSERTION_DATE_RECOGNITION_PARAMETER = 2;
    private static final int INSERTION_AMOUNT_PARAMETER = 3;

    private static final String SELECTION_BY_ID_QUERY_TEMPLATE =
            "SELECT revenue_recognition.contract_id, " +
                    "revenue_recognition.date_recognition, " +
                    "revenue_recognition.amount, " +
                    "FROM revenue_recognition " +
                    "INNER JOIN contracts " +
                    "ON revenue_recognition.contract_id = contracts.date_recognition <= ? " +
                    "WHERE revenue_recognition.contract_id = ? AND recognizedOn <=";
    private static final int SELECTION_BY_ID_CONTRACT_ID_PARAMETER = 1;
    private static final int SELECTION_BY_ID_DATE_RECOGNITION_PARAMETER = 2;

    private final static Logger logger = Logger.getLogger(RevenueRecognitionJdbcGateway.class);

    public void save(int contractId, LocalDate dateRecognition, BigDecimal amount) {

        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERTION_QUERY_TEMPLATE);) {
            statement.setInt(INSERTION_CONTRACT_ID_PARAMETER, contractId);
            statement.setDate(INSERTION_DATE_RECOGNITION_PARAMETER, Date.valueOf(dateRecognition));
            statement.setBigDecimal(INSERTION_AMOUNT_PARAMETER, amount);
            statement.addBatch();
            statement.executeBatch();
        } catch (SQLException e) {
            logger.error("Cannot save revenue recognition", e);
        }
    }

    public ResultSet findByContractIdOnDate(int contractId, LocalDate dateRecognition) {

        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECTION_BY_ID_QUERY_TEMPLATE)) {
            statement.setInt(SELECTION_BY_ID_CONTRACT_ID_PARAMETER, contractId);
            statement.setDate(SELECTION_BY_ID_DATE_RECOGNITION_PARAMETER, Date.valueOf(dateRecognition));
            ResultSet resultSet = statement.executeQuery();
            return resultSet;
        } catch (SQLException e) {
            logger.error("Cannot find revenue recognition by contract ID", e);
        }

        return null;
    }

}
