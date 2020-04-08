package com.isaev.ee.simplefactory.namefactory;

import com.isaev.ee.simplefactory.namers.FirstFirst;
import com.isaev.ee.simplefactory.namers.LastFirst;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NameFactoryTest {

    private final NameFactory factory = new NameFactory();

    @Test
    void shouldCreateFirstName() {

        var first = factory.getName("first");
        assertTrue(first instanceof FirstFirst);

    }

    @Test
    void shouldCreateLastName() {

        var first = factory.getName("last");
        assertTrue(first instanceof LastFirst);

    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenTakesWrongName(){
        assertThrows(IllegalArgumentException.class, () -> factory.getName("this is a wrong name"));
    }

}