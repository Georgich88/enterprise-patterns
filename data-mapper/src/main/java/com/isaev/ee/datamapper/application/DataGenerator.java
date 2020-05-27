package com.isaev.ee.datamapper.application;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.isaev.ee.datamapper.people.Person;
import com.isaev.ee.datamapper.people.PersonMapper;
import com.isaev.ee.datamapper.database.exception.DataMapperException;
import org.apache.log4j.Logger;

import java.util.stream.IntStream;

public class DataGenerator {

    private Faker randomDataGenerator;
    private static final Logger logger = Logger.getLogger(DataGenerator.class);
    private static final PersonMapper personMapper = new PersonMapper();

    public static final String MESSAGE_TEMPLATE_CANNOT_SAVE_THE_PERSON = "Cannot save the person with ID: %s";


    public DataGenerator() {
        randomDataGenerator = new Faker();
    }


    /**
     * Generate sample people
     *
     * @param number - number of people to generate
     */
    public void generatePeople(int number) {

        IntStream.range(0, number).forEach(id -> {
            try {
                personMapper.insert(createRandomPerson(id));
            } catch (DataMapperException e) {
                var message = String.format(MESSAGE_TEMPLATE_CANNOT_SAVE_THE_PERSON, id);
                logger.error(message, e);
            }
        });

    }

    private Person createRandomPerson(int id) {
        Name name = randomDataGenerator.name();
        String email = randomDataGenerator.internet().emailAddress();
        return new Person(id, name.firstName(), name.lastName(), email);
    }

}
