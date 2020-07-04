package com.isaev.ee.frontcontroller.scripts;

import com.isaev.ee.frontcontroller.database.jdbc.ContractJdbcGateway;
import com.isaev.ee.frontcontroller.database.jdbc.RevenueRecognitionJdbcGateway;
import com.isaev.ee.frontcontroller.database.utils.ConnectionUtils;
import org.apache.log4j.Logger;
import org.javamoney.moneta.Money;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Calculates the revenue recognitions for a contract
 *
 * @author Georgy Isaev
 */
public class RevenueRecognitionCommand implements TransactionScriptCommand {

    private static final String MESSAGE_TEMPLATE_CANNOT_FIND_CONTRACT = "Cannot find contract by ID: %s";
    private static final ContractJdbcGateway CONTRACT_JDBC_GATEWAY = new ContractJdbcGateway();
    private static final RevenueRecognitionJdbcGateway REVENUE_RECOGNITION_JDBC_GATEWAY = new RevenueRecognitionJdbcGateway();
    private static final String WORD_PROCESSORS_CONTRACT_TYPE = "W";
    private static final String DATABASES_CONTRACT_TYPE = "D";
    private static final String SPREADSHEETS_CONTRACT_TYPE = "S";
    private static final String TYPE_FIELD_NAME = "type";
    private static final String DATE_SIGNED_FIELD_NAME = "date_signed";
    private static final String REVENUE_FIELD_NAME = "revenue";
    private static final String USD_CURRENCY_CODE = "USD";

    private final static Logger logger = Logger.getLogger(RevenueRecognitionCommand.class);

    private int contractId;

    public RevenueRecognitionCommand(int contractId) {
        this.contractId = contractId;
    }

    @Override
    public void run() {
        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = getPreparedStatementFindContractById(connection)) {

            ResultSet contract = retrieveContract(statement);
            Money totalRevenue = contractTotalRevenue(contract);
            LocalDate recognitionDate = contractDateSigned(contract);
            String type = contractType(contract);

            recognizeRevenueByType(totalRevenue, recognitionDate, type);

        } catch (SQLException e) {
            logger.error(String.format(MESSAGE_TEMPLATE_CANNOT_FIND_CONTRACT, contractId), e);
        }
    }

    private PreparedStatement getPreparedStatementFindContractById(Connection connection) throws SQLException {
        return CONTRACT_JDBC_GATEWAY.getPreparedStatementFindById(connection, contractId);
    }

    private ResultSet retrieveContract(PreparedStatement statement) throws SQLException {
        ResultSet contract = statement.executeQuery();
        contract.next();
        return contract;
    }

    protected void recognizeRevenueByType(Money totalRevenue, LocalDate recognitionDate, String type) {
        if (type.equals(SPREADSHEETS_CONTRACT_TYPE)) {
            recognizeContractRevenueInThirds(totalRevenue, recognitionDate, 30, 60, 90);
        } else if (type.equals(WORD_PROCESSORS_CONTRACT_TYPE)) {
            recognizeContractRevenueOnDate(totalRevenue, recognitionDate);
        } else if (type.equals(DATABASES_CONTRACT_TYPE)) {
            recognizeContractRevenueInThirds(totalRevenue, recognitionDate, 0, 30, 60);
        }
    }

    protected void recognizeContractRevenueInThirds(Money totalRevenue, LocalDate recognitionDate, int firstThreshold, int secondThreshold, int thirdThreshold) {
        Money[] allocation = totalRevenue.divideAndRemainder(3);
        Money quotient = allocation[0];
        Money reminder = allocation[1];
        recognizeContractRevenueOnDate(quotient, recognitionDate.plusDays(firstThreshold));
        recognizeContractRevenueOnDate(quotient, recognitionDate.plusDays(secondThreshold));
        recognizeContractRevenueOnDate(quotient.add(reminder), recognitionDate.plusDays(thirdThreshold));
    }

    private void recognizeContractRevenueOnDate(Money totalRevenue, LocalDate recognitionDate) {
        REVENUE_RECOGNITION_JDBC_GATEWAY.save(contractId, recognitionDate, totalRevenue.getNumberStripped());
    }

    private String contractType(ResultSet contracts) throws SQLException {
        return contracts.getString(TYPE_FIELD_NAME);
    }

    private LocalDate contractDateSigned(ResultSet contracts) throws SQLException {
        return contracts.getDate(DATE_SIGNED_FIELD_NAME).toLocalDate();
    }

    private Money contractTotalRevenue(ResultSet contract) throws SQLException {
        return Money.of(contract.getBigDecimal(REVENUE_FIELD_NAME), USD_CURRENCY_CODE);
    }
}
