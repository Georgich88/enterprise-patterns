package com.isaev.ee.transformview.transformers.people;

import com.isaev.ee.transformview.people.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonToXmlTransformerTest {

    public static final String SERIALIZED_PERSON = "<person id=\"1\"><firstName>Jon</firstName><lastName>Jones</lastName><email>jon.jones@gmail.com</email></person>";

    @Test
    void shouldTransform() {
        Person person = new Person(1, "Jon", "Jones", "jon.jones@gmail.com");
        String personXmlString = assertDoesNotThrow(() -> new PersonToXmlTransformer(person).transform());
        assertEquals(SERIALIZED_PERSON, personXmlString);

    }
}