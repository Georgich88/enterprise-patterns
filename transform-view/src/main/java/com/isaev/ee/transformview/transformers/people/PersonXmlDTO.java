package com.isaev.ee.transformview.transformers.people;

import com.isaev.ee.transformview.people.Person;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Maps a person to an XML elements.
 */
@XmlRootElement(name = "person")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonXmlDTO implements Serializable {

    @XmlAttribute
    @XmlID
    private int id;
    @XmlElement
    private String firstName;
    @XmlElement
    private String lastName;
    @XmlElement
    private String email;

    public PersonXmlDTO() {
    }

    public PersonXmlDTO(Person person) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.email = person.getEmail();
    }

    public Person toPerson() {
        return new Person(id, firstName, lastName, email);
    }

}
