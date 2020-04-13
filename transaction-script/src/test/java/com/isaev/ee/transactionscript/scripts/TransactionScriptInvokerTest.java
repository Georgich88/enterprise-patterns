package com.isaev.ee.transactionscript.scripts;

import com.isaev.ee.transactionscript.application.DataGenerator;
import com.isaev.ee.transactionscript.database.jdbc.ContractJdbcGateway;
import com.isaev.ee.transactionscript.database.utils.QueryExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;

class TransactionScriptInvokerTest {

    DataGenerator dataGenerator = new DataGenerator();

    @BeforeEach
    public void initiateDatabaseBeforeTests() {
        QueryExecutor.createUser();
        QueryExecutor.createTables();
        dataGenerator.generateProducts();
    }

    @Test
    void shouldRecognizeRevenue(){
        var scriptInvoker = new TransactionScriptInvoker();
        ContractJdbcGateway contractJdbcGateway = new ContractJdbcGateway();
        contractJdbcGateway.save(0,0,LocalDate.of(2020, 1,1), BigDecimal.valueOf(1_000_000));
        contractJdbcGateway.save(1,1,LocalDate.of(2020, 1,1), BigDecimal.valueOf(1_000_000));
        contractJdbcGateway.save(2,2,LocalDate.of(2020, 1,1), BigDecimal.valueOf(1_000_000));
        scriptInvoker.addCommand(new RevenueRecognitionCommand(0));
        scriptInvoker.addCommand(new RevenueRecognitionCommand(1));
        scriptInvoker.addCommand(new RevenueRecognitionCommand(2));
        var spyRecognizedRevenueContract = Mockito.spy(new RecognizedRevenueCommand(1, LocalDate.of(2020, 2,1)));
        scriptInvoker.addCommand(spyRecognizedRevenueContract);
        scriptInvoker.runCommands();
        Mockito.verify(spyRecognizedRevenueContract, Mockito.atLeastOnce()).recognizedRevenue();
    }

}