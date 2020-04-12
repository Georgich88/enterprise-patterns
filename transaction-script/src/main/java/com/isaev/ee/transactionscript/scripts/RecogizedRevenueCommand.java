package com.isaev.ee.transactionscript.scripts;

import com.isaev.ee.transactionscript.database.jdbc.RevenueRecognitionJdbcGateway;
import org.javamoney.moneta.Money;

import java.sql.SQLException;
import java.time.LocalDate;

public class RecogizedRevenueCommand implements TransactionScriptCommand {

    private static final String USD_CURRENCY_CODE = "USD";

    private int contractId;
    private LocalDate asOf;

    private static final RevenueRecognitionJdbcGateway REVENUE_RECOGNITION_JDBC_GATEWAY = new RevenueRecognitionJdbcGateway();


    public RecogizedRevenueCommand(int contractId, LocalDate asOf) {
        this.contractId = contractId;
        this.asOf = asOf;
    }

    @Override
    public void run() {

        Money result = recognizedRevenue();
        System.out.println(result);
    }

    public Money recognizedRevenue() {
        Money result = Money.of(0, USD_CURRENCY_CODE);
        try {
            var revenueRecognition = REVENUE_RECOGNITION_JDBC_GATEWAY.findByContractIdOnDate(contractId, asOf);
            while (revenueRecognition.next()) {
                result.add(Money.of(revenueRecognition.getBigDecimal("amount"), "USD"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


}
