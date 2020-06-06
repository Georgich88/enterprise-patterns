package com.isaev.ee.transformview.transformers.people;

import com.isaev.ee.transformview.people.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class XmlToPersonTransformerTest {

    public static final String SERIALIZED_PERSON = "<person id=\"1\"><firstName>Jon</firstName><lastName>Jones</lastName><email>jon.jones@gmail.com</email></person>";

    @Test
    void shouldTransform() {
        Person deserializedPerson = assertDoesNotThrow(() -> new XmlToPersonTransformer(SERIALIZED_PERSON).transform());
        assertEquals(1, deserializedPerson.getId());
        assertEquals("Jon", deserializedPerson.getFirstName());
        assertEquals("Jones", deserializedPerson.getLastName());
        assertEquals("jon.jones@gmail.com", deserializedPerson.getEmail());
    }
}