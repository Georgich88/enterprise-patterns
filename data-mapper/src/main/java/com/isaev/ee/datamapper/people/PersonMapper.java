package com.isaev.ee.datamapper.people;

import com.isaev.ee.datamapper.database.datamapper.DataMapper;
import com.isaev.ee.datamapper.database.exception.DataMapperException;
import com.isaev.ee.datamapper.database.utils.ConnectionUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class PersonMapper implements DataMapper<Person> {

    private static final String INSERTION_QUERY_TEMPLATE = "INSERT INTO persons (person_id, first_name, second_name, email_id) VALUES (?, ?, ?, ?)";
    private static final int INSERTION_PERSON_ID_PARAMETER = 1;
    private static final int INSERTION_FIRST_NAME_PARAMETER = 2;
    private static final int INSERTION_SECOND_NAME_PARAMETER = 3;
    private static final int INSERTION_EMAIL_ID_PARAMETER = 4;

    private static final String UPDATING_QUERY_TEMPLATE = "UPDATE persons SET first_name = ?, second_name = ?, email_id = ? WHERE person_id = ?";
    private static final int UPDATING_FIRST_NAME_PARAMETER = 1;
    private static final int UPDATING_SECOND_NAME_PARAMETER = 2;
    private static final int UPDATING_EMAIL_ID_PARAMETER = 3;
    private static final int UPDATING_PERSON_ID_PARAMETER = 4;

    private static final String DELETION_QUERY_TEMPLATE = "DELETE FROM public.persons  WHERE person_id = ?";
    private static final int DELETION_PERSON_ID_PARAMETER = 1;

    private static final String SELECTION_BY_ID_QUERY_TEMPLATE =
            "SELECT persons.person_id, " +
                    "persons.first_name, " +
                    "persons.second_name, " +
                    "persons.email_id " +
                    "FROM persons " +
                    "WHERE persons.person_id = ?";
    private static final int SELECTION_BY_ID_PERSON_ID_PARAMETER = 1;
    private static final String FIRST_NAME_FIELD_NAME = "first_name";
    private static final String SECOND_NAME_FIELD_NAME = "second_name";
    private static final String EMAIL_FIELD_NAME = "email";

    private static final String MESSAGE_TEMPLATE_CANNOT_FIND_PERSON = "Cannot find person by ID: %s";
    public static final String MESSAGE_TEMPLATE_CANNOT_SAVE_THE_PERSON = "Cannot save the person with ID: %s";
    public static final String MESSAGE_TEMPLATE_CANNOT_DELETE_THE_PERSON = "Cannot remove the person with ID: %s";

    private final static Logger logger = Logger.getLogger(PersonMapper.class);

    // Constructors

    public PersonMapper() {
    }

    // Database operation logic

    public static Optional<Person> findById(int personId) throws DataMapperException{

        Optional<Person> person;

        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECTION_BY_ID_QUERY_TEMPLATE)) {
            statement.setInt(SELECTION_BY_ID_PERSON_ID_PARAMETER, personId);

            var resultSet = statement.executeQuery();
            resultSet.next();

            String firstName = resultSet.getString(FIRST_NAME_FIELD_NAME);
            String secondName = resultSet.getString(SECOND_NAME_FIELD_NAME);
            String email = resultSet.getString(EMAIL_FIELD_NAME);

            person = Optional.of(new Person(personId, firstName, secondName, email));

        } catch (SQLException e) {
            var message = String.format(MESSAGE_TEMPLATE_CANNOT_FIND_PERSON, personId);
            logger.error(message, e);
            throw new DataMapperException(message, e);
        }

        return person;

    }

    @Override
    public void update(Person person) throws DataMapperException {
        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATING_QUERY_TEMPLATE)) {
            statement.setInt(UPDATING_PERSON_ID_PARAMETER, person.getId());
            statement.setString(UPDATING_FIRST_NAME_PARAMETER, person.getFirstName());
            statement.setString(UPDATING_SECOND_NAME_PARAMETER, person.getLastName());
            statement.setInt(UPDATING_EMAIL_ID_PARAMETER, 0);//person.getEmail());
            statement.addBatch();
            statement.executeBatch();
        } catch (SQLException e) {
            var message = String.format(MESSAGE_TEMPLATE_CANNOT_SAVE_THE_PERSON, person.getId());
            logger.error(message, e);
            throw new DataMapperException(message, e);
        }
    }

    @Override
    public void insert(Person person) throws DataMapperException {
        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERTION_QUERY_TEMPLATE)) {
            statement.setInt(INSERTION_PERSON_ID_PARAMETER, person.getId());
            statement.setString(INSERTION_FIRST_NAME_PARAMETER, person.getFirstName());
            statement.setString(INSERTION_SECOND_NAME_PARAMETER, person.getLastName());
            statement.setInt(INSERTION_EMAIL_ID_PARAMETER, 0);//person.getEmail());
            statement.addBatch();
            statement.executeBatch();
        } catch (SQLException e) {
            var message = String.format(MESSAGE_TEMPLATE_CANNOT_SAVE_THE_PERSON, person.getId());
            logger.error(message, e);
            throw new DataMapperException(message, e);
        }
    }

    @Override
    public void delete(Person person) throws DataMapperException {
        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETION_QUERY_TEMPLATE)) {
            statement.setInt(DELETION_PERSON_ID_PARAMETER, person.getId());
            statement.addBatch();
            statement.executeBatch();
        } catch (SQLException e) {
            var message = String.format(MESSAGE_TEMPLATE_CANNOT_DELETE_THE_PERSON, person.getId());
            logger.error(message, e);
            throw new DataMapperException(message, e);
        }
    }

}
