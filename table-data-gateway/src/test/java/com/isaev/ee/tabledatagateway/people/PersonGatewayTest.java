package com.isaev.ee.tabledatagateway.people;

import com.isaev.ee.tabledatagateway.database.exception.DataGatewayException;
import com.isaev.ee.tabledatagateway.database.gateway.DataGateway;
import com.isaev.ee.tabledatagateway.database.gateway.PersonGateway;
import com.isaev.ee.tabledatagateway.database.utils.QueryExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonGatewayTest {

    private DataGateway<Person, Integer> personGateway = new PersonGateway();

    @BeforeEach
    public void initiateDatabaseBeforeTests() {
        QueryExecutor.createTables();
    }

    @Test
    @Order(1)
    public void shouldSavePerson(){
        Person personJonJones = new Person(0, "Jon", "Jones", "jon.jones@gmail.com");
        Person finalPersonJonJones = personJonJones;
        assertDoesNotThrow(() -> personGateway.save(finalPersonJonJones));
        personJonJones = assertDoesNotThrow(() -> personGateway.findById(0)).get();
        assertEquals("Jon", personJonJones.getFirstName());
        assertEquals("Jones", personJonJones.getLastName());
        assertEquals("jon.jones@gmail.com", personJonJones.getEmail());
    }

    @Test
    @Order(2)
    public void shouldUpdatePerson(){
        Person personJonJones = new Person(0, "Jon", "Jones", "jon.jones@gmail.com");
        Person finalPersonJonJones = personJonJones;
        assertDoesNotThrow(() -> personGateway.save(finalPersonJonJones));
        personJonJones.setEmail("jon.jones@yandex.ru");
        personJonJones.setFirstName("Jonie");
        personJonJones.setLastName("Bones");
        assertDoesNotThrow(() -> personGateway.update(finalPersonJonJones));
        personJonJones = assertDoesNotThrow(() -> personGateway.findById(0)).get();
        assertEquals("Jonie", personJonJones.getFirstName());
        assertEquals("Bones", personJonJones.getLastName());
        assertEquals("jon.jones@yandex.ru", personJonJones.getEmail());
    }

    @Test
    @Order(3)
    public void shouldRemovePerson(){
        Person personJonJones = new Person(0, "Jon", "Jones", "jon.jones@gmail.com");
        final Person finalPersonJonJones = personJonJones;
        assertDoesNotThrow(() -> personGateway.save(finalPersonJonJones));
        assertDoesNotThrow(() -> personGateway.delete(finalPersonJonJones));
        assertThrows(DataGatewayException.class,() -> personGateway.findById(0));

    }

}