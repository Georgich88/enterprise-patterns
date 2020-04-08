package com.isaev.ee.simplefactory.namers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LastFirstTest {

    @Test
    void shouldCreateLastFirstTest(){
        assertEquals("last", new LastFirst("last").getlName());
    }
}