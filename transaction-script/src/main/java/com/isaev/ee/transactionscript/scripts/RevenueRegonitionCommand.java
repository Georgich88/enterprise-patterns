package com.isaev.ee.transactionscript.scripts;

import com.isaev.ee.transactionscript.database.jdbc.ContractJdbcGateway;
import com.isaev.ee.transactionscript.database.jdbc.RevenueRecognitionJdbcGateway;
import org.javamoney.moneta.Money;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class RevenueRegonitionCommand implements TransactionScriptCommand {

    private int contractId;
    private static final ContractJdbcGateway CONTRACT_JDBC_GATEWAY = new ContractJdbcGateway();
    private static final RevenueRecognitionJdbcGateway REVENUE_RECOGNITION_JDBC_GATEWAY = new RevenueRecognitionJdbcGateway();

    private static final String WORD_PROCESSORS_CONTRACT_TYPE = "W";
    private static final String DATABASES_CONTRACT_TYPE = "D";
    private static final String SPREADSHEETS_CONTRACT_TYPE = "S";

    private static final String TYPE_FIELD_NAME = "type";
    private static final String DATE_SIGNED_FIELD_NAME = "dateSigned";
    private static final String REVENUE_FIELD_NAME = "revenue";

    private static final String USD_CURRENCY_CODE = "USD";

    public RevenueRegonitionCommand(int contractId) {
        this.contractId = contractId;
    }

    @Override
    public void run() {

        try {

            ResultSet contracts = CONTRACT_JDBC_GATEWAY.findById(contractId);
            contracts.next();
            Money totalRevenue = contractTotalRevenue(contracts);
            LocalDate recognitionDate = contractDateSigned(contracts);
            String type = contractType(contracts);

            recognizeRevenueByType(totalRevenue, recognitionDate, type);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void recognizeRevenueByType(Money totalRevenue, LocalDate recognitionDate, String type) {
        if (type.equals(SPREADSHEETS_CONTRACT_TYPE)) {
            recognizeContractRevenueInThirds(totalRevenue, recognitionDate, 30, 60, 90);
        } else if (type.equals(WORD_PROCESSORS_CONTRACT_TYPE)) {
            recognizeContractRevenueOnDate(totalRevenue, recognitionDate);
        } else if (type.equals(DATABASES_CONTRACT_TYPE)) {
            recognizeContractRevenueInThirds(totalRevenue, recognitionDate, 0, 30, 60);
        }
    }

    private void recognizeContractRevenueInThirds(Money totalRevenue, LocalDate recognitionDate, int firstThreshold, int secondThreshold, int thirdThreshold) {
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
