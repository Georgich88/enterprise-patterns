package com.isaev.ee.activerecord.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    @Test
    public void shouldInitiateDatabase() {
        assertDoesNotThrow(() -> Application.initiateDatabase());
    }

    @Test
    public void shouldGenerateData() {
        assertDoesNotThrow(() -> Application.generateData());
    }
}