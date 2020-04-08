package com.isaev.ee.simplefactory.namers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FirstFirstTest {

    @Test
    void shouldCreateFirstFirstTest(){
        assertEquals("first", new FirstFirst("first").getFrName());
    }

}