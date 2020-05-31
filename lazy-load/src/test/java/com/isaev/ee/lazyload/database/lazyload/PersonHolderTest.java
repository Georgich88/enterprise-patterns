package com.isaev.ee.lazyload.database.lazyload;

import com.isaev.ee.lazyload.database.utils.QueryExecutor;
import com.isaev.ee.lazyload.people.Person;
import com.isaev.ee.lazyload.people.PersonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonHolderTest {

    private static final PersonMapper personMapper = new PersonMapper();

    @BeforeEach
    public void initiateDatabaseBeforeTests() {
        QueryExecutor.createTables();
    }
    @Test

    @Order(1)
    public void shouldLazilyLoadPerson(){
        Person personJonJones = new Person(0, "Jon", "Jones", "jon.jones@gmail.com");
        Person finalPersonJonJones = personJonJones;
        assertDoesNotThrow(() -> personMapper.insert(finalPersonJonJones));
        PersonHolder personHolderJonJones = assertDoesNotThrow(() -> new PersonHolder(personMapper, 0));
        personJonJones = personHolderJonJones.getValue();
        assertEquals("Jon", personJonJones.getFirstName());
        assertEquals("Jones", personJonJones.getLastName());
        assertNull(personJonJones.getEmail());
        String email = assertDoesNotThrow(() -> personHolderJonJones.getEmail());
        assertEquals("jon.jones@gmail.com", email);
    }

}