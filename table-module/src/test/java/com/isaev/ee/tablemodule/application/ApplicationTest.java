package com.isaev.ee.tablemodule.application;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    @Test
    @Order(1)
    public void shouldInitiateDatabase() {
        assertDoesNotThrow(() -> {
            Application.initiateDatabase();
        });
    }

    @Test
    @Order(2)
    public void shouldGenerateData() {
        assertDoesNotThrow(() -> {
            Application.generateData();
        });
    }
}