package com.isaev.ee.datamapper.application;

import com.isaev.ee.datamapper.people.PersonMapper;
import com.isaev.ee.datamapper.database.exception.DataMapperException;
import com.isaev.ee.datamapper.database.utils.QueryExecutor;
import org.apache.log4j.Logger;

import java.util.stream.IntStream;

public class Application {

    private static final Logger logger = Logger.getLogger(Application.class);
    private static final PersonMapper personMapper = new PersonMapper();

    private static final int NUMBER_OF_PEOPLE_TO_CREATE = 100;
    private static final String MESSAGE_TEMPLATE_CANNOT_FIND_PERSON = "Cannot find person by ID: %s";

    public static void main(String[] args) {
        initiateDatabase();
        generateData();
        runApplication();
    }

    private static void runApplication() {
        IntStream.range(0, NUMBER_OF_PEOPLE_TO_CREATE).forEach(id -> {
            try {
                System.out.println(personMapper.findById(id).orElseThrow());
            } catch (DataMapperException e) {
                var message = String.format(MESSAGE_TEMPLATE_CANNOT_FIND_PERSON, id);
                logger.error(message, e);
            }
        });
    }

    protected static void initiateDatabase() {
        QueryExecutor.createUser();
        QueryExecutor.createDatabase();
        QueryExecutor.createTables();
    }

    private static void deleteDatabase() {
        QueryExecutor.deleteDatabase();
    }

    protected static void generateData() {
        DataGenerator dataGenerator = new DataGenerator();
        dataGenerator.generatePeople(NUMBER_OF_PEOPLE_TO_CREATE);
    }

}
