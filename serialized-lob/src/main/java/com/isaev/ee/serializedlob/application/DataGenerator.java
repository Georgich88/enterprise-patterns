package com.isaev.ee.serializedlob.application;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.isaev.ee.serializedlob.database.exception.DataMapperException;
import com.isaev.ee.serializedlob.people.Customer;
import com.isaev.ee.serializedlob.people.CustomerMapper;
import org.apache.log4j.Logger;

import java.util.stream.IntStream;

public class DataGenerator {

    public static final String MESSAGE_TEMPLATE_CANNOT_SAVE_THE_CUSTOMER = "Cannot save the customer with ID: %s";
    private static final Logger logger = Logger.getLogger(DataGenerator.class);
    private static final CustomerMapper customerMapper = new CustomerMapper();
    private final Faker randomDataGenerator;


    public DataGenerator() {
        randomDataGenerator = new Faker();
    }


    /**
     * Generate sample customers
     *
     * @param number - number of people to generate
     */
    public void generateCustomers(int number) {

        IntStream.range(0, number).forEach(id -> {
            try {
                customerMapper.insert(createRandomCustomer(id));
            } catch (DataMapperException e) {
                var message = String.format(MESSAGE_TEMPLATE_CANNOT_SAVE_THE_CUSTOMER, id);
                logger.error(message, e);
            }
        });

    }

    private Customer createRandomCustomer(int id) {
        Name name = randomDataGenerator.name();
        String email = randomDataGenerator.internet().emailAddress();
        return new Customer(id, name.firstName(), name.lastName(), email);
    }

}
