package com.isaev.ee.cacheaccessor.dataaccess;

import com.isaev.ee.cacheaccessor.domain.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataAccessorTest {
    @Test
    void shouldFindFirstPerson(){
        DataAccessible<Person> accessor = new DataAccessor();
        assertEquals(new Person("First", "Person"), accessor.findFirst().orElseThrow());
    }

    @Test
    void shouldFindLastPerson(){
        DataAccessible<Person> accessor = new DataAccessor();
        assertEquals(new Person("Last", "Person"), accessor.findLast().orElseThrow());
    }
}