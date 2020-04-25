package com.isaev.ee.tablemodule.service;

import com.isaev.ee.tablemodule.database.jdbc.ContractJdbcGateway;
import com.isaev.ee.tablemodule.database.jdbc.ProductJdbcGateway;
import com.isaev.ee.tablemodule.database.jdbc.RevenueRecognitionJdbcGateway;
import com.isaev.ee.tablemodule.tables.Contract;
import com.isaev.ee.tablemodule.tables.Product;
import com.isaev.ee.tablemodule.tables.RevenueRecognition;
import org.javamoney.moneta.Money;

import java.time.LocalDate;
import java.util.List;

public class RevenueService {

    private static final String WORD_PROCESSORS_CONTRACT_TYPE = "W";
    private static final String DATABASES_CONTRACT_TYPE = "D";
    private static final String SPREADSHEETS_CONTRACT_TYPE = "S";

    private static final ContractJdbcGateway contractJdbcGateway = new ContractJdbcGateway();
    private static final ProductJdbcGateway productJdbcGateway = new ProductJdbcGateway();
    private static final RevenueRecognitionJdbcGateway revenueRecognitionJdbcGateway = new RevenueRecognitionJdbcGateway();

    private static final String USD_CURRENCY_CODE = "USD";

    public static void recognizeRevenue(int contractId) {

        var contract = contractJdbcGateway.findByContracId(contractId).orElseThrow();
        recognizeContractRevenue(contract);
    }

    /**
     * Retrieves total contract revenue amount from the database.
     *
     * @return overall revenue amount
     */
    public static Money retrieveRecognizedRevenue(int contractId, LocalDate dateRecognition) {

        var revenueRecognitions = revenueRecognitionJdbcGateway.findByContractIdOnDate(contractId, dateRecognition);
        return revenueRecognitions.stream().reduce(
                Money.of(0, USD_CURRENCY_CODE),
                (total, revenueRecognition)->total.add(revenueRecognition.getAmount()),
                (money, money2) -> money.add(money2));

    }

    private static void recognizeContractRevenue(Contract contract) {
        Product product = productJdbcGateway.findByProductId(contract.getProductId()).orElseThrow();
        String type = product.getType();
        if (type.equals(SPREADSHEETS_CONTRACT_TYPE)) {
            recognizeContractRevenueInThirds(contract, 30, 60, 90);
        } else if (type.equals(WORD_PROCESSORS_CONTRACT_TYPE)) {
            recognizeContractRevenueOnDate(new RevenueRecognition(contract.getId(), contract.getDateSigned(), contract.getRevenue()));
        } else if (type.equals(DATABASES_CONTRACT_TYPE)) {
            recognizeContractRevenueInThirds(contract, 0, 30, 60);
        }
    }

    private static void recognizeContractRevenueInThirds(Contract contract, int firstThreshold, int secondThreshold, int thirdThreshold) {

        Money[] allocation = contract.getRevenue().divideAndRemainder(3);
        Money quotient = allocation[0];
        Money reminder = allocation[1];

        LocalDate dateSigned = contract.getDateSigned();

        var revenueRecognitions = List.of(new RevenueRecognition(contract.getId(), dateSigned.plusDays(firstThreshold), quotient),
                new RevenueRecognition(contract.getId(), dateSigned.plusDays(secondThreshold), quotient),
                new RevenueRecognition(contract.getId(), dateSigned.plusDays(thirdThreshold), quotient.add(reminder)));

        revenueRecognitionJdbcGateway.saveAll(revenueRecognitions);
    }

    private static void recognizeContractRevenueOnDate(RevenueRecognition revenueRecognition) {
        revenueRecognitionJdbcGateway.save(revenueRecognition);
    }

}
