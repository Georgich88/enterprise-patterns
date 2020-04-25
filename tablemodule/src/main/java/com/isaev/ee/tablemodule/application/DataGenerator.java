package com.isaev.ee.tablemodule.application;

import com.isaev.ee.tablemodule.database.jdbc.ContractJdbcGateway;
import com.isaev.ee.tablemodule.database.jdbc.ProductJdbcGateway;
import com.isaev.ee.tablemodule.tables.Contract;
import com.isaev.ee.tablemodule.tables.Product;
import org.javamoney.moneta.Money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.stream.IntStream;

public class DataGenerator {

    private static final int MAX_CONTRACT_REVENUE = 100_000_000;
    private static final String USD_CURRENCY_CODE = "USD";
    private Random randomNumberGenerator;

    public DataGenerator() {
        randomNumberGenerator = new Random();
    }


    /**
     * Generates default products: word processors, databases and spreadsheets.
     */
    public void generateProducts() {
        var productJdbcGateway = new ProductJdbcGateway();
        productJdbcGateway.save(new Product(0, "word processors", "W"));
        productJdbcGateway.save(new Product(1, "databases,", "D"));
        productJdbcGateway.save(new Product(2, "spreadsheets", "S"));
    }

    /**
     * Generate sample contracts
     *
     * @param number - number of contracts to generate
     */
    public void generateContracts(int number) {

        var contractJdbcGateway = new ContractJdbcGateway();

        IntStream.range(0, number).forEach(contractId -> contractJdbcGateway.save(
                new Contract(contractId,
                        generateRandomNumberInRange(0, 2),
                        generateRandomDate(),
                        Money.of(BigDecimal.valueOf(generateRandomNumberInRange(0, MAX_CONTRACT_REVENUE)), USD_CURRENCY_CODE))));


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
