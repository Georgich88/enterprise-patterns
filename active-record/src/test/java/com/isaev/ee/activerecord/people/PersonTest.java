package com.isaev.ee.activerecord.people;

import com.isaev.ee.activerecord.database.exception.ActiveRecordException;
import com.isaev.ee.activerecord.database.utils.QueryExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @BeforeEach
    public void initiateDatabaseBeforeTests() {
        QueryExecutor.createTables();
    }

    @Test
    @Order(1)
    public void shouldSavePerson(){
        Person personJonJones = new Person(0, "Jon", "Jones", "jon.jones@gmail.com");
        Person finalPersonJonJones = personJonJones;
        assertDoesNotThrow(() -> finalPersonJonJones.insert());
        personJonJones = assertDoesNotThrow(() -> Person.findById(0)).get();
        assertEquals("Jon", personJonJones.getFirstName());
        assertEquals("Jones", personJonJones.getLastName());
        assertEquals("jon.jones@gmail.com", personJonJones.getEmail());
    }

    @Test
    @Order(2)
    public void shouldUpdatePerson(){
        Person personJonJones = new Person(0, "Jon", "Jones", "jon.jones@gmail.com");
        Person finalPersonJonJones = personJonJones;
        assertDoesNotThrow(() -> finalPersonJonJones.insert());
        personJonJones.setEmail("jon.jones@yandex.ru");
        personJonJones.setFirstName("Jonie");
        personJonJones.setLastName("Bones");
        assertDoesNotThrow(() -> finalPersonJonJones.update());
        personJonJones = assertDoesNotThrow(() -> Person.findById(0)).get();
        assertEquals("Jonie", personJonJones.getFirstName());
        assertEquals("Bones", personJonJones.getLastName());
        assertEquals("jon.jones@yandex.ru", personJonJones.getEmail());
    }

    @Test
    @Order(3)
    public void shouldRemovePerson(){
        Person personJonJones = new Person(0, "Jon", "Jones", "jon.jones@gmail.com");
        final Person finalPersonJonJones = personJonJones;
        assertDoesNotThrow(() -> finalPersonJonJones.insert());
        assertDoesNotThrow(() -> finalPersonJonJones.delete());
        assertThrows(ActiveRecordException.class,() -> Person.findById(0));

    }

}