package com.isaev.ee.frontcontroller.database.jdbc;

import com.isaev.ee.frontcontroller.database.utils.ConnectionUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class RevenueRecognitionJdbcGateway {
    private static final String INSERTION_QUERY_TEMPLATE = "INSERT INTO revenue_recognition (contract_id, date_recognition, amount) VALUES (?, ?, ?)";
    private static final int INSERTION_CONTRACT_ID_PARAMETER = 1;
    private static final int INSERTION_DATE_RECOGNITION_PARAMETER = 2;
    private static final int INSERTION_AMOUNT_PARAMETER = 3;
    private static final String SELECTION_BY_ID_QUERY_TEMPLATE =
            "SELECT revenue_recognition.contract_id, " +
                    "revenue_recognition.date_recognition, " +
                    "revenue_recognition.amount " +
                    "FROM revenue_recognition " +
                    "INNER JOIN contracts " +
                    "ON revenue_recognition.contract_id = contracts.contract_id " +
                    "WHERE revenue_recognition.contract_id = ? AND revenue_recognition.date_recognition  <= ?  ";
    private static final int SELECTION_BY_ID_CONTRACT_ID_PARAMETER = 1;
    private static final int SELECTION_BY_ID_DATE_RECOGNITION_PARAMETER = 2;
    public static final String MESSAGE_TEMPLATE_CANNOT_SAVE_REVENUE_RECOGNITION = "Cannot save revenue recognition for contract ID: %s";

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
            logger.error(String.format(MESSAGE_TEMPLATE_CANNOT_SAVE_REVENUE_RECOGNITION, contractId), e);
        }
    }

    public PreparedStatement getPreparedStatementFindByContractIdOnDate(Connection connection, int contractId, LocalDate dateRecognition) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SELECTION_BY_ID_QUERY_TEMPLATE);
        statement.setInt(SELECTION_BY_ID_CONTRACT_ID_PARAMETER, contractId);
        statement.setDate(SELECTION_BY_ID_DATE_RECOGNITION_PARAMETER, Date.valueOf(dateRecognition));
        return statement;
    }


}
