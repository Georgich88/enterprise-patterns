package com.isaev.ee.transactionscript.application;

import com.isaev.ee.transactionscript.database.utils.QueryExecutor;
import com.isaev.ee.transactionscript.scripts.RecogizedRevenueCommand;
import com.isaev.ee.transactionscript.scripts.RevenueRegonitionCommand;
import com.isaev.ee.transactionscript.scripts.TransactionScriptInvoker;

import java.time.LocalDate;
import java.util.stream.IntStream;

public class Application {

    public static void main(String[] args) {

        initiateDatabase();
        generateData();
        runScript();
        deleteDatabase();
    }

    public static void runScript() {
        var scriptInvoker = new TransactionScriptInvoker();
        var currentDate = LocalDate.now();
        IntStream.range(0, 100).forEach(contractId -> {
            scriptInvoker.addCommand(new RevenueRegonitionCommand(contractId));
            scriptInvoker.addCommand(new RecogizedRevenueCommand(contractId, currentDate));
        });

        scriptInvoker.runCommands();
    }

    public static void initiateDatabase() {

        QueryExecutor.createUser();
        QueryExecutor.createDatabase();
        QueryExecutor.createTables();

    }

    public static void deleteDatabase() {
        QueryExecutor.deleteDatabase();
    }

    public static void generateData() {

        DataGenerator dataGenerator = new DataGenerator();
        dataGenerator.generateProducts();
        dataGenerator.generateContracts(100);

    }

}
