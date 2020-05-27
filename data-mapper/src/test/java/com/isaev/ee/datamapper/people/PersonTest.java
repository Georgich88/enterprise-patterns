package com.isaev.ee.datamapper.people;

import com.isaev.ee.datamapper.database.exception.DataMapperException;
import com.isaev.ee.datamapper.database.utils.QueryExecutor;
import com.isaev.ee.datamapper.people.Person;
import com.isaev.ee.datamapper.people.PersonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    private static final PersonMapper personMapper = new PersonMapper();

    @BeforeEach
    public void initiateDatabaseBeforeTests() {
        QueryExecutor.createTables();
    }

    @Test
    @Order(1)
    public void shouldSavePerson(){
        Person personJonJones = new Person(0, "Jon", "Jones", "jon.jones@gmail.com");
        Person finalPersonJonJones = personJonJones;
        assertDoesNotThrow(() -> personMapper.insert(finalPersonJonJones));
        personJonJones = assertDoesNotThrow(() -> personMapper.findById(0).orElseThrow());
        assertEquals("Jon", personJonJones.getFirstName());
        assertEquals("Jones", personJonJones.getLastName());
        assertEquals("jon.jones@gmail.com", personJonJones.getEmail());
    }

    @Test
    @Order(2)
    public void shouldUpdatePerson(){
        Person personJonJones = new Person(0, "Jon", "Jones", "jon.jones@gmail.com");
        Person finalPersonJonJones = personJonJones;
        assertDoesNotThrow(() -> personMapper.insert(finalPersonJonJones));
        personJonJones.setEmail("jon.jones@yandex.ru");
        personJonJones.setFirstName("Jonie");
        personJonJones.setLastName("Bones");
        assertDoesNotThrow(() -> personMapper.update(finalPersonJonJones));
        personJonJones = assertDoesNotThrow(() -> personMapper.findById(0).orElseThrow());
        assertEquals("Jonie", personJonJones.getFirstName());
        assertEquals("Bones", personJonJones.getLastName());
        assertEquals("jon.jones@yandex.ru", personJonJones.getEmail());
    }

    @Test
    @Order(3)
    public void shouldRemovePerson(){
        Person personJonJones = new Person(0, "Jon", "Jones", "jon.jones@gmail.com");
        final Person finalPersonJonJones = personJonJones;
        assertDoesNotThrow(() -> personMapper.insert(finalPersonJonJones));
        assertDoesNotThrow(() -> personMapper.delete(finalPersonJonJones));
        assertThrows(DataMapperException.class,() -> personMapper.findById(0));

    }
}