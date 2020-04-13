package com.isaev.ee.transactionscript.application;

import com.isaev.ee.transactionscript.database.utils.QueryExecutor;
import com.isaev.ee.transactionscript.scripts.RecognizedRevenueCommand;
import com.isaev.ee.transactionscript.scripts.RevenueRecognitionCommand;
import com.isaev.ee.transactionscript.scripts.TransactionScriptInvoker;

import java.time.LocalDate;
import java.util.stream.IntStream;

public class Application {

    private static final int NUMBER_OF_CONTRACTS_TO_CREATE = 100;
    private static final int YEARS_OFFSET_TO_CHECK_RECOGNIZED_REVENUE = 1;

    public static void main(String[] args) {
        initiateDatabase();
        generateData();
        runScripts();
        deleteDatabase();
    }

    private static void runScripts() {
        var scriptInvoker = new TransactionScriptInvoker();
        var currentDate = LocalDate.now();
        IntStream.range(0, NUMBER_OF_CONTRACTS_TO_CREATE).forEach(contractId -> {
            scriptInvoker.addCommand(new RevenueRecognitionCommand(contractId));
            scriptInvoker.addCommand(new RecognizedRevenueCommand(contractId,
                    currentDate.plusYears(YEARS_OFFSET_TO_CHECK_RECOGNIZED_REVENUE)));
        });
        scriptInvoker.runCommands();
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
