package com.isaev.ee.transactionscript.application;

import com.isaev.ee.transactionscript.database.jdbc.ContractJdbcGateway;
import com.isaev.ee.transactionscript.database.jdbc.ProductJdbcGateway;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.stream.IntStream;

public class DataGenerator {

    private static final int MAX_CONTRACT_REVENUE = 100_000_000;

    private Random randomNumberGenerator;

    public DataGenerator() {
        randomNumberGenerator = new Random();
    }


    /**
     * Generates default products: word processors, databases and spreadsheets.
     */
    public void generateProducts() {
        var productJdbcGateway = new ProductJdbcGateway();
        productJdbcGateway.save(0, "word processors", "W");
        productJdbcGateway.save(1, "databases,", "D");
        productJdbcGateway.save(2, "spreadsheets", "S");
    }

    /**
     * Generate sample contracts
     *
     * @param number - number of contracts to generate
     */
    public void generateContracts(int number) {

        var contractJdbcGateway = new ContractJdbcGateway();

        IntStream.range(0, number).forEach(contractId -> contractJdbcGateway.save(
                contractId,
                generateRandomNumberInRange(0, 2),
                generateRandomDate(),
                BigDecimal.valueOf(generateRandomNumberInRange(0, MAX_CONTRACT_REVENUE))));


    }

    private int generateRandomNumberInRange(int min, int max) {
        return randomNumberGenerator
                .ints(min, (max + 1))
                .limit(1)
                .findFirst()
                .getAsInt();
    }

    private LocalDate generateRandomDate() {
        LocalDate start = LocalDate.of(2020, Month.JANUARY, 1);
        long days = ChronoUnit.DAYS.between(start, LocalDate.now());
        LocalDate randomDate = start.plusDays(randomNumberGenerator.nextInt((int) days + 1));
        return randomDate;
    }

}
