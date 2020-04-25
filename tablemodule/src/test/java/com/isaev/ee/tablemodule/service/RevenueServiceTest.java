package com.isaev.ee.tablemodule.service;

import com.isaev.ee.tablemodule.application.DataGenerator;
import com.isaev.ee.tablemodule.database.jdbc.ContractJdbcGateway;
import com.isaev.ee.tablemodule.database.utils.QueryExecutor;
import com.isaev.ee.tablemodule.tables.Contract;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RevenueServiceTest {
    private static final String USD_CURRENCY_CODE = "USD";
    DataGenerator dataGenerator = new DataGenerator();

    @BeforeEach
    public void initiateDatabaseBeforeTests() {
        QueryExecutor.createUser();
        QueryExecutor.createTables();
        dataGenerator.generateProducts();
    }

    @Test
    void shouldRecognizeRevenue() {

        ContractJdbcGateway contractJdbcGateway = new ContractJdbcGateway();
        final var dateToTest =  LocalDate.of(2020, 2, 1);

        List<Contract> contracts = new ArrayList<>(3);

        contracts.add(new Contract(0, 0, LocalDate.of(2020, 1, 1), Money.of(BigDecimal.valueOf(1_000_000), USD_CURRENCY_CODE)));
        contracts.add(new Contract(1, 1, LocalDate.of(2020, 1, 1), Money.of(BigDecimal.valueOf(1_000_000), USD_CURRENCY_CODE)));
        contracts.add(new Contract(2, 2, LocalDate.of(2020, 1, 1), Money.of(BigDecimal.valueOf(1_000_000), USD_CURRENCY_CODE)));

        contracts.forEach(contract -> contractJdbcGateway.save(contract));
        contracts.forEach(contract -> RevenueService.recognizeRevenue(contract.getId()));

        assertEquals(Money.of(1000000, USD_CURRENCY_CODE),RevenueService.retrieveRecognizedRevenue(0, dateToTest) );
        assertEquals(Money.of(666666, USD_CURRENCY_CODE),RevenueService.retrieveRecognizedRevenue(1, dateToTest) );
        assertEquals(Money.of(333333, USD_CURRENCY_CODE),RevenueService.retrieveRecognizedRevenue(2, dateToTest) );

    }

}