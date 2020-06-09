package com.isaev.ee.transformview.transformers.people;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.isaev.ee.transformview.people.Person;
import com.isaev.ee.transformview.transformers.Transformer;

import java.io.IOException;

/**
 * Transforms a XML string to a person.
 *
 * @author Georgy Isaev
 */
public class XmlToPersonTransformer implements Transformer<String, Person> {

    private final String person;

    public XmlToPersonTransformer(String person) {
        this.person = person;
    }

    @Override
    public Person transform() throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JaxbAnnotationModule());
        PersonXmlDTO personXmlDTO = xmlMapper.readValue(this.person, PersonXmlDTO.class);
        return personXmlDTO.toPerson();
    }
}
