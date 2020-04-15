package com.isaev.ee.tablemodule.database.jdbc;

import com.isaev.ee.tablemodule.database.utils.ConnectionUtils;
import com.isaev.ee.tablemodule.tables.Product;
import com.isaev.ee.tablemodule.tables.RevenueRecognition;
import org.apache.log4j.Logger;
import org.javamoney.moneta.Money;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RevenueRecognitionJdbcGateway {

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
    public static final String MESSAGE_TEMPLATE_CANNOT_SAVE_REVENUE_RECOGNITION = "Cannot save revenue recognitions";
    private static final String CONTRACT_ID_NAME = "contract_id";
    private static final String DATE_RECOGNITION_FIELD_NAME = "date_recognition";
    private static final String AMOUNT_FIELD_NAME = "amount";
    private static final String USD_CURRENCY_CODE = "USD";
    public static final String MESSAGE_TEMPLATE_CANNOT_FIND_REVENUE_RECOGNITION_BY_CONTRACT_ID = "Cannot find revenue recognition by contract ID: %s";

    private static final String INSERTION_QUERY_TEMPLATE = "INSERT INTO revenue_recognition (contract_id, date_recognition, amount) VALUES (?, ?, ?)";
    private static final int INSERTION_CONTRACT_ID_PARAMETER = 1;
    private static final int INSERTION_DATE_RECOGNITION_PARAMETER = 2;
    private static final int INSERTION_AMOUNT_PARAMETER = 3;


    private final static Logger logger = Logger.getLogger(RevenueRecognitionJdbcGateway.class);

    public void saveAll(List<RevenueRecognition> revenueRecognitions) {

        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERTION_QUERY_TEMPLATE)) {

            prepareInsertionRevenueRecognitionStatements(revenueRecognitions, statement);
            statement.executeBatch();

        } catch (SQLException e) {
            logger.error(MESSAGE_TEMPLATE_CANNOT_SAVE_REVENUE_RECOGNITION, e);
        }

    }

    public void save(RevenueRecognition revenueRecognitions) {
        saveAll(List.of(revenueRecognitions));
    }

    private void prepareInsertionRevenueRecognitionStatements(List<RevenueRecognition> revenueRecognitions, PreparedStatement statement) throws SQLException {

        for (var revenueRecognition : revenueRecognitions) {

            statement.setInt(INSERTION_CONTRACT_ID_PARAMETER, revenueRecognition.getContractId());
            statement.setDate(INSERTION_DATE_RECOGNITION_PARAMETER, Date.valueOf(revenueRecognition.getDateRecognition()));
            statement.setBigDecimal(INSERTION_AMOUNT_PARAMETER, revenueRecognition.getAmount().getNumberStripped());
            statement.addBatch();

        }

    }

    public List<RevenueRecognition> findByContractIdOnDate(int contractId, LocalDate dateRecognition) {

        List<RevenueRecognition> revenueRecognitions = new ArrayList<>();

        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECTION_BY_ID_QUERY_TEMPLATE)) {

            statement.setInt(SELECTION_BY_ID_CONTRACT_ID_PARAMETER, contractId);
            statement.setDate(SELECTION_BY_ID_DATE_RECOGNITION_PARAMETER, Date.valueOf(dateRecognition));

            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                revenueRecognitions.add(new RevenueRecognition(
                        resultSet.getInt(CONTRACT_ID_NAME),
                        resultSet.getDate(DATE_RECOGNITION_FIELD_NAME).toLocalDate(),
                        Money.of(resultSet.getBigDecimal(AMOUNT_FIELD_NAME), USD_CURRENCY_CODE)));
            }

        } catch (SQLException e) {
            logger.error(String.format(MESSAGE_TEMPLATE_CANNOT_FIND_REVENUE_RECOGNITION_BY_CONTRACT_ID, contractId), e);
        }

        return revenueRecognitions;
    }

}
