package com.isaev.ee.transactionscript.scripts;

import com.isaev.ee.transactionscript.database.jdbc.RevenueRecognitionJdbcGateway;
import com.isaev.ee.transactionscript.database.utils.ConnectionUtils;
import org.apache.log4j.Logger;
import org.javamoney.moneta.Money;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class RecognizedRevenueCommand implements TransactionScriptCommand {

    private static final String USD_CURRENCY_CODE = "USD";
    public static final String MESSAGE_TEMPLATE_CANNOT_FIND_REVENUE_RECOGNITION_BY_CONTRACT_ID = "Cannot find revenue recognition by contract ID: %s";
    private static final RevenueRecognitionJdbcGateway REVENUE_RECOGNITION_JDBC_GATEWAY = new RevenueRecognitionJdbcGateway();

    private final static Logger logger = Logger.getLogger(RecognizedRevenueCommand.class);

    private int contractId;
    private LocalDate dateRecognition;

    public RecognizedRevenueCommand(int contractId, LocalDate dateRecognition) {
        this.contractId = contractId;
        this.dateRecognition = dateRecognition;
    }

    @Override
    public void run() {

        Money result = recognizedRevenue();
        System.out.println(result);
    }

    /**
     * Retrieves total contract revenue amount from the database.
     *
     * @return overall revenue amount
     */
    public Money recognizedRevenue() {

        Money result = Money.of(0, USD_CURRENCY_CODE);

        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = REVENUE_RECOGNITION_JDBC_GATEWAY.getPreparedStatementFindByContractIdOnDate(connection, contractId, dateRecognition);) {
            var revenueRecognition = statement.executeQuery();
            while (revenueRecognition.next()) {
                result = result.add(Money.of(revenueRecognition.getBigDecimal("amount"), "USD"));
            }

        } catch (SQLException e) {
            logger.error(String.format(MESSAGE_TEMPLATE_CANNOT_FIND_REVENUE_RECOGNITION_BY_CONTRACT_ID, contractId), e);
        }

        return result;
    }


}
