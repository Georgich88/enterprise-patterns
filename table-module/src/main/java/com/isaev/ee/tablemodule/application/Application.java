package com.isaev.ee.tablemodule.application;

import com.isaev.ee.tablemodule.database.utils.QueryExecutor;
import com.isaev.ee.tablemodule.service.RevenueService;

import java.time.LocalDate;
import java.util.stream.IntStream;

public class Application {

    private static final int NUMBER_OF_CONTRACTS_TO_CREATE = 100;
    private static final int YEARS_OFFSET_TO_CHECK_RECOGNIZED_REVENUE = 1;

    public static void main(String[] args) {
        initiateDatabase();
        generateData();
        runService();
        deleteDatabase();
    }

    private static void runService() {
        var currentDate = LocalDate.now();
        IntStream.range(0, NUMBER_OF_CONTRACTS_TO_CREATE).forEach(contractId -> {
            RevenueService.recognizeRevenue(contractId);
            var recognizedRevenue = RevenueService.retrieveRecognizedRevenue(contractId,
                    currentDate.plusYears(YEARS_OFFSET_TO_CHECK_RECOGNIZED_REVENUE));
            System.out.println(recognizedRevenue);
        });
    }

    protected static void initiateDatabase() {
        QueryExecutor.createUser();
        QueryExecutor.createDatabase();
        QueryExecutor.createTables();
    }

    private static void deleteDatabase() {
        QueryExecutor.deleteDatabase();
    }

    protected static void generateData() {
        DataGenerator dataGenerator = new DataGenerator();
        dataGenerator.generateProducts();
        dataGenerator.generateContracts(100);
    }


}
