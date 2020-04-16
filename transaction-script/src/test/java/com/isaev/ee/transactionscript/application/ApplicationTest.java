package com.isaev.ee.transactionscript.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ApplicationTest {
    
    @Test
    public void shouldInitiateDatabase() {
        assertDoesNotThrow(() -> {
            Application.initiateDatabase();
        });
    }

    @Test
    public void shouldGenerateData() {
        assertDoesNotThrow(() -> {
            Application.generateData();
        });
    }
}