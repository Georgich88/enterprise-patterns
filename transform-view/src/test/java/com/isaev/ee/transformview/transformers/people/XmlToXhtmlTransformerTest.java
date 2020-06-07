package com.isaev.ee.transformview.transformers.people;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlToXhtmlTransformerTest {

    public static final String SERIALIZED_PERSON = "<person id=\"1\"><firstName>Jon</firstName><lastName>Jones</lastName><email>jon.jones@gmail.com</email></person>";

    @Test
    void shouldTransform() {

        String html = assertDoesNotThrow(() -> new XmlToXhtmlTransformer(SERIALIZED_PERSON).transform());
        assertTrue(html.contains("input type=\"text\" name=\"email\" value=\"jon.jones@gmail.com\""));

    }
}