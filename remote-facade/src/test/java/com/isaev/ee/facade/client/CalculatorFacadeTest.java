package com.isaev.ee.facade.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorFacadeTest {

    @Test
    void add() {
        assertDoesNotThrow(() -> {
            assertEquals(11, CalculatorFacade.add(5, 6));
        });
    }

    @Test
    void subtract() {
        assertDoesNotThrow(() -> {
            assertEquals(1, CalculatorFacade.subtract(6, 5));
        });
    }

    @Test
    void multiply() {
        assertDoesNotThrow(() -> {
            assertEquals(30, CalculatorFacade.multiply(5, 6));
        });

    }

    @Test
    void divide() {
        assertDoesNotThrow(() -> {
            assertEquals(-1, CalculatorFacade.divide(5, 6));
        });

    }

}