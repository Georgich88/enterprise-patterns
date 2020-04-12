package com.isaev.ee.transactionscript.scripts;

import com.isaev.ee.transactionscript.database.jdbc.ContractJdbcGateway;
import com.isaev.ee.transactionscript.database.jdbc.RevenueRecognitionJdbcGateway;
import org.javamoney.moneta.Money;

import java.sql.SQLException;
import java.time.LocalDate;

public class RevenueRegonitionCommand implements TransactionScriptCommand {

    private int contractId;
    private static final ContractJdbcGateway CONTRACT_JDBC_GATEWAY = new ContractJdbcGateway();
    private static final RevenueRecognitionJdbcGateway REVENUE_RECOGNITION_JDBC_GATEWAY = new RevenueRecognitionJdbcGateway();

    public RevenueRegonitionCommand(int contractId) {
        this.contractId = contractId;
    }

    @Override
    public void run() {

        try {
            var contracts = CONTRACT_JDBC_GATEWAY.findById(contractId);
            contracts.next();
            Money totalRevenue = Money.of(contracts.getBigDecimal("revenue"), "USD");
            LocalDate recognitionDate = contracts.getDate("dateSigned").toLocalDate();
            String type = contracts.getString("type");
            if (type.equals("S")) {
                Money[] allocation = totalRevenue.divideAndRemainder(3);
                Money quotient = allocation[0];
                Money reminder = allocation[1];
                REVENUE_RECOGNITION_JDBC_GATEWAY.save
                        (contractId, recognitionDate.plusDays(30), quotient.getNumberStripped());
                REVENUE_RECOGNITION_JDBC_GATEWAY.save
                        (contractId, recognitionDate.plusDays(60), quotient.getNumberStripped());
                REVENUE_RECOGNITION_JDBC_GATEWAY.save
                        (contractId, recognitionDate.plusDays(90), quotient.add(reminder).getNumberStripped());
            } else if (type.equals("W")) {
                REVENUE_RECOGNITION_JDBC_GATEWAY.save(contractId, recognitionDate, totalRevenue.getNumberStripped());
            } else if (type.equals("D")) {
                Money[] allocation = totalRevenue.divideAndRemainder(3);
                Money quotient = allocation[0];
                Money reminder = allocation[1];
                REVENUE_RECOGNITION_JDBC_GATEWAY.save
                        (contractId, recognitionDate, quotient.getNumberStripped());
                REVENUE_RECOGNITION_JDBC_GATEWAY.save
                        (contractId, recognitionDate.plusDays(30), quotient.getNumberStripped());
                REVENUE_RECOGNITION_JDBC_GATEWAY.save
                        (contractId, recognitionDate.plusDays(60), quotient.add(reminder).getNumberStripped());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
