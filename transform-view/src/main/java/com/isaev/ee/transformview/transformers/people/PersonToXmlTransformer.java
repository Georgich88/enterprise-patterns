package com.isaev.ee.transformview.transformers.people;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.isaev.ee.transformview.people.Person;
import com.isaev.ee.transformview.transformers.Transformer;

/**
 * Transfers a person to XML string.
 *
 * @author Georgy Isaev
 */
public class PersonToXmlTransformer implements Transformer<Person, String> {

    private final PersonXmlDTO person;

    public PersonToXmlTransformer(Person person) {
        this.person = new PersonXmlDTO(person);
    }

    public String transform() throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JaxbAnnotationModule());
        String xmlString = xmlMapper.writeValueAsString(this.person);
        return xmlString;
    }
}
