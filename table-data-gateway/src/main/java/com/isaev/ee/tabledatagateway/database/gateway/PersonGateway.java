package com.isaev.ee.tabledatagateway.database.gateway;

import com.isaev.ee.tabledatagateway.database.exception.DataGatewayException;
import com.isaev.ee.tabledatagateway.database.utils.ConnectionUtils;
import com.isaev.ee.tabledatagateway.people.Person;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class PersonGateway implements DataGateway<Person, Integer> {

    private static final String INSERTION_QUERY_TEMPLATE = "INSERT INTO people (person_id, first_name, second_name, email) VALUES (?, ?, ?, ?)";
    private static final int INSERTION_PERSON_ID_PARAMETER = 1;
    private static final int INSERTION_FIRST_NAME_PARAMETER = 2;
    private static final int INSERTION_SECOND_NAME_PARAMETER = 3;
    private static final int INSERTION_EMAIL_PARAMETER = 4;

    private static final String UPDATING_QUERY_TEMPLATE = "UPDATE people SET first_name = ?, second_name = ?, email = ? WHERE person_id = ?";
    private static final int UPDATING_FIRST_NAME_PARAMETER = 1;
    private static final int UPDATING_SECOND_NAME_PARAMETER = 2;
    private static final int UPDATING_EMAIL_PARAMETER = 3;
    private static final int UPDATING_PERSON_ID_PARAMETER = 4;

    private static final String DELETION_QUERY_TEMPLATE = "DELETE FROM public.people  WHERE person_id = ?";
    private static final int DELETION_PERSON_ID_PARAMETER = 1;

    private static final String SELECTION_BY_ID_QUERY_TEMPLATE =
            "SELECT people.person_id, " +
                    "people.first_name, " +
                    "people.second_name, " +
                    "people.email " +
                    "FROM people " +
                    "WHERE people.person_id = ?";
    private static final int SELECTION_BY_ID_PERSON_ID_PARAMETER = 1;
    private static final String FIRST_NAME_FIELD_NAME = "first_name";
    private static final String SECOND_NAME_FIELD_NAME = "second_name";
    private static final String EMAIL_FIELD_NAME = "email";

    private static final String MESSAGE_TEMPLATE_CANNOT_FIND_PERSON = "Cannot find person by ID: %s";
    public static final String MESSAGE_TEMPLATE_CANNOT_SAVE_THE_PERSON = "Cannot save the person with ID: %s";
    public static final String MESSAGE_TEMPLATE_CANNOT_DELETE_THE_PERSON = "Cannot remove the person with ID: %s";

    private final static Logger logger = Logger.getLogger(PersonGateway.class);

    // Constructors

    public PersonGateway() {
    }

    // Database operation logic

    @Override
    public Optional<Person> findById(Integer personId) throws DataGatewayException {

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
            throw new DataGatewayException(message, e);
        }

        return person;

    }

    @Override
    public void update(Person person) throws DataGatewayException {
        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATING_QUERY_TEMPLATE)) {
            statement.setInt(UPDATING_PERSON_ID_PARAMETER, person.getId());
            statement.setString(UPDATING_FIRST_NAME_PARAMETER, person.getFirstName());
            statement.setString(UPDATING_SECOND_NAME_PARAMETER, person.getLastName());
            statement.setString(UPDATING_EMAIL_PARAMETER, person.getEmail());
            statement.addBatch();
            statement.executeBatch();
        } catch (SQLException e) {
            var message = String.format(MESSAGE_TEMPLATE_CANNOT_SAVE_THE_PERSON, person.getId());
            logger.error(message, e);
            throw new DataGatewayException(message, e);
        }
    }

    @Override
    public void save(Person person) throws DataGatewayException {
        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERTION_QUERY_TEMPLATE)) {
            statement.setInt(INSERTION_PERSON_ID_PARAMETER, person.getId());
            statement.setString(INSERTION_FIRST_NAME_PARAMETER, person.getFirstName());
            statement.setString(INSERTION_SECOND_NAME_PARAMETER, person.getLastName());
            statement.setString(INSERTION_EMAIL_PARAMETER, person.getEmail());
            statement.addBatch();
            statement.executeBatch();
        } catch (SQLException e) {
            var message = String.format(MESSAGE_TEMPLATE_CANNOT_SAVE_THE_PERSON, person.getId());
            logger.error(message, e);
            throw new DataGatewayException(message, e);
        }
    }

    @Override
    public void delete(Person person) throws DataGatewayException {
        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETION_QUERY_TEMPLATE)) {
            statement.setInt(DELETION_PERSON_ID_PARAMETER, person.getId());
            statement.addBatch();
            statement.executeBatch();
        } catch (SQLException e) {
            var message = String.format(MESSAGE_TEMPLATE_CANNOT_DELETE_THE_PERSON, person.getId());
            logger.error(message, e);
            throw new DataGatewayException(message, e);
        }
    }

}
