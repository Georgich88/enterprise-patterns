package com.isaev.ee.transformview.application;

import com.isaev.ee.transformview.people.Person;
import com.isaev.ee.transformview.transformers.people.PersonToXmlTransformer;
import com.isaev.ee.transformview.transformers.people.XmlToPersonTransformer;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        Person person = new Person(1, "Jon", "Jones", "jon.jones@gmail.com");
        String personXmlString = new PersonToXmlTransformer(person).transform();
        System.out.println(personXmlString);

        Person deserializedPerson = new XmlToPersonTransformer(personXmlString).transform();
        System.out.println(deserializedPerson);
    }
}
