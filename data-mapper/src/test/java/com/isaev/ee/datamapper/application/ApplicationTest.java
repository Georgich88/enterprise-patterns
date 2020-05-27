package com.isaev.ee.datamapper.application;

import com.isaev.ee.datamapper.application.Application;
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