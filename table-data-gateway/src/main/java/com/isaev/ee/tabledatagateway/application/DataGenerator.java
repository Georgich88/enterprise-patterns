package com.isaev.ee.tabledatagateway.application;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.isaev.ee.tabledatagateway.database.exception.DataGatewayException;
import com.isaev.ee.tabledatagateway.database.gateway.DataGateway;
import com.isaev.ee.tabledatagateway.database.gateway.PersonGateway;
import com.isaev.ee.tabledatagateway.people.Person;
import org.apache.log4j.Logger;

import java.util.stream.IntStream;

public class DataGenerator {

    private Faker randomDataGenerator;
    public static final String MESSAGE_TEMPLATE_CANNOT_SAVE_THE_PERSON = "Cannot save the person with ID: %s";
    private final static Logger logger = Logger.getLogger(DataGenerator.class);
    private DataGateway personGateway;

    public DataGenerator() {
        randomDataGenerator = new Faker();
        personGateway = new PersonGateway();
    }


    /**
     * Generate sample people
     *
     * @param number - number of people to generate
     */
    public void generatePeople(int number) {

        IntStream.range(0, number).forEach(id -> {
            try {
                personGateway.save(createRandomPerson(id));
            } catch (DataGatewayException e) {
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
