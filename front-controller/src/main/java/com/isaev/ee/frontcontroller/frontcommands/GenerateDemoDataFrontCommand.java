package com.isaev.ee.frontcontroller.frontcommands;

import com.isaev.ee.frontcontroller.application.DataGenerator;
import com.isaev.ee.frontcontroller.database.utils.QueryExecutor;
import com.isaev.ee.frontcontroller.scripts.RevenueRecognitionCommand;
import com.isaev.ee.frontcontroller.scripts.TransactionScriptInvoker;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.stream.IntStream;

/**
 * Generates demo contracts from front.
 *
 * @author Georgy Isaev
 */
public class GenerateDemoDataFrontCommand extends FrontCommand {

    private static final int NUMBER_OF_CONTRACTS_TO_CREATE = 100;

    @Override
    public void process() throws ServletException, IOException {
        try {
            initiateDatabase();
            generateData();
            runScripts();
            forward("demo-data-generated");
        } catch (Exception exception) {
            request.setAttribute("error-message", exception.getMessage());
            forward("unknown");
        }
    }

    private void runScripts() throws Exception {
        var scriptInvoker = new TransactionScriptInvoker();
        IntStream.range(0, NUMBER_OF_CONTRACTS_TO_CREATE).forEach(contractId -> {
            scriptInvoker.addCommand(new RevenueRecognitionCommand(contractId));
        });
        scriptInvoker.runCommands();
    }

    private static void initiateDatabase() {
        QueryExecutor.createUser();
        QueryExecutor.createDatabase();
        QueryExecutor.createTables();
    }

    private static void generateData() {
        DataGenerator dataGenerator = new DataGenerator();
        dataGenerator.generateProducts();
        dataGenerator.generateContracts(NUMBER_OF_CONTRACTS_TO_CREATE);
    }
}
