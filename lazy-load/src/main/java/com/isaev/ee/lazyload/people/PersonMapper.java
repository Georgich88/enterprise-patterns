package com.isaev.ee.lazyload.people;

import com.isaev.ee.lazyload.database.datamapper.DataMapper;
import com.isaev.ee.lazyload.database.exception.DataMapperException;
import com.isaev.ee.lazyload.database.utils.ConnectionUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class PersonMapper implements DataMapper<Person> {

    private static final String INSERTION_EMAILS_QUERY_TEMPLATE = "INSERT INTO public.emails(email_id, email) VALUES (nextval('emails_id_seq'), ?);";
    private static final int INSERTION_EMAILS_EMAIL_PARAMETER = 1;

    private static final String INSERTION_QUERY_TEMPLATE = "INSERT INTO public.persons(person_id, first_name, second_name, email_id) VALUES (?, ?, ?, currval('emails_id_seq'));";
    private static final int INSERTION_PERSON_ID_PARAMETER = 1;
    private static final int INSERTION_FIRST_NAME_PARAMETER = 2;
    private static final int INSERTION_SECOND_NAME_PARAMETER = 3;

    private static final String UPDATING_EMAILS_QUERY_TEMPLATE = "UPDATE emails SET email = ? WHERE email_id = ?;";
    private static final int UPDATING_EMAILS_EMAIL_PARAMETER = 1;
    private static final int UPDATING_EMAILS_EMAIL_ID_PARAMETER = 2;

    private static final String UPDATING_QUERY_TEMPLATE = "UPDATE persons SET first_name = ?, second_name = ?, email_id = ? WHERE person_id = ?";
    private static final int UPDATING_FIRST_NAME_PARAMETER = 1;
    private static final int UPDATING_SECOND_NAME_PARAMETER = 2;
    private static final int UPDATING_EMAIL_ID_PARAMETER = 3;
    private static final int UPDATING_PERSON_ID_PARAMETER = 4;

    private static final String DELETION_QUERY_TEMPLATE = "DELETE FROM public.persons  WHERE person_id = ?";
    private static final int DELETION_PERSON_ID_PARAMETER = 1;

    private static final String DELETION_EMAILS_QUERY_TEMPLATE = "DELETE FROM public.emails  WHERE email_id = ?";
    private static final int DELETION_EMAILS_EMAIL_ID_PARAMETER = 1;

    private static final String SELECTION_BY_ID_QUERY_TEMPLATE =
            "SELECT persons.person_id, " +
                    "persons.first_name, " +
                    "persons.second_name, " +
                    "emails.email " +
                    "FROM persons " +
                    "LEFT JOIN emails " +
                    "ON persons.email_id = emails.email_id " +
                    "WHERE persons.person_id = ?";
    private static final int SELECTION_BY_ID_PERSON_ID_PARAMETER = 1;
    private static final String FIRST_NAME_FIELD_NAME = "first_name";
    private static final String SECOND_NAME_FIELD_NAME = "second_name";
    private static final String EMAIL_FIELD_NAME = "email";
    private static final String EMAIL_ID_FIELD_NAME = "email_id";

    private static final String SELECTION_BY_ID_LAZILY_QUERY_TEMPLATE =
            "SELECT persons.person_id, " +
                    "persons.first_name, " +
                    "persons.second_name " +
                    "FROM persons " +
                    "WHERE persons.person_id = ?";
    private static final int SELECTION_BY_ID_LAZILY_PERSON_ID_PARAMETER = 1;


    private static final String SELECTION_EMAIL_ID_BY_PERSON_ID_QUERY_TEMPLATE =
            "SELECT persons.email_id " +
                    "FROM persons " +
                    "WHERE persons.person_id = ?";
    private static final int SELECTION_EMAIL_ID_BY_PERSON_ID_PARAMETER = 1;

    private static final String SELECTION_EMAIL_BY_PERSON_ID_QUERY_TEMPLATE =
            "SELECT emails.email " +
                    "FROM persons " +
                    "LEFT JOIN emails " +
                    "ON persons.email_id = emails.email_id " +
                    "WHERE persons.person_id = ?";
    private static final int SELECTION_EMAIL_BY_PERSON_ID_PARAMETER = 1;

    private static final String MESSAGE_TEMPLATE_CANNOT_FIND_PERSON = "Cannot find person by ID: %s";
    public static final String MESSAGE_TEMPLATE_CANNOT_SAVE_THE_PERSON = "Cannot save the person with ID: %s";
    public static final String MESSAGE_TEMPLATE_CANNOT_DELETE_THE_PERSON = "Cannot remove the person with ID: %s";

    private final static Logger logger = Logger.getLogger(PersonMapper.class);

    // Constructors

    public PersonMapper() {
    }

    // Database operation logic

    public static Optional<Integer> findEmailIdByPersonId(int personId) throws DataMapperException {

        Optional<Integer> emailId;

        try (Connection connection = ConnectionUtils.getConnection();
             var statement = connection.prepareStatement(SELECTION_EMAIL_ID_BY_PERSON_ID_QUERY_TEMPLATE)) {

            statement.setInt(SELECTION_EMAIL_ID_BY_PERSON_ID_PARAMETER, personId);
            var resultSet = statement.executeQuery();
            resultSet.next();

            emailId = Optional.of(resultSet.getInt(EMAIL_ID_FIELD_NAME));

        } catch (SQLException e) {
            var message = String.format(MESSAGE_TEMPLATE_CANNOT_FIND_PERSON, personId);
            logger.error(message, e);
            throw new DataMapperException(message, e);
        }

        return emailId;

    }

    public static Optional<String> findEmailByPersonId(int personId) throws DataMapperException {

        Optional<String> emailId;

        try (Connection connection = ConnectionUtils.getConnection();
             var statement = connection.prepareStatement(SELECTION_EMAIL_BY_PERSON_ID_QUERY_TEMPLATE)) {

            statement.setInt(SELECTION_EMAIL_BY_PERSON_ID_PARAMETER, personId);
            var resultSet = statement.executeQuery();
            resultSet.next();

            emailId = Optional.of(resultSet.getString(EMAIL_FIELD_NAME));

        } catch (SQLException e) {
            var message = String.format(MESSAGE_TEMPLATE_CANNOT_FIND_PERSON, personId);
            logger.error(message, e);
            throw new DataMapperException(message, e);
        }

        return emailId;

    }

    public static Optional<Person> findById(int personId) throws DataMapperException {

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

    public static Optional<Person> findByIdLazily(int personId) throws DataMapperException {

        Optional<Person> person;

        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECTION_BY_ID_LAZILY_QUERY_TEMPLATE)) {
            statement.setInt(SELECTION_BY_ID_LAZILY_PERSON_ID_PARAMETER, personId);

            var resultSet = statement.executeQuery();
            resultSet.next();

            String firstName = resultSet.getString(FIRST_NAME_FIELD_NAME);
            String secondName = resultSet.getString(SECOND_NAME_FIELD_NAME);

            person = Optional.of(new Person(personId, firstName, secondName));

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
             var personStatement = connection.prepareStatement(UPDATING_QUERY_TEMPLATE);
             var emailStatement = connection.prepareStatement(UPDATING_EMAILS_QUERY_TEMPLATE);) {

            connection.setAutoCommit(false);

            int emailId = findEmailIdByPersonId(person.getId()).orElseThrow();

            emailStatement.setString(UPDATING_EMAILS_EMAIL_PARAMETER, person.getEmail());
            emailStatement.setInt(UPDATING_EMAILS_EMAIL_ID_PARAMETER, emailId);
            emailStatement.addBatch();
            emailStatement.executeBatch();

            personStatement.setInt(UPDATING_PERSON_ID_PARAMETER, person.getId());
            personStatement.setString(UPDATING_FIRST_NAME_PARAMETER, person.getFirstName());
            personStatement.setString(UPDATING_SECOND_NAME_PARAMETER, person.getLastName());
            personStatement.setInt(UPDATING_EMAIL_ID_PARAMETER, emailId);
            personStatement.addBatch();
            personStatement.executeBatch();

            connection.commit();

        } catch (SQLException e) {
            var message = String.format(MESSAGE_TEMPLATE_CANNOT_SAVE_THE_PERSON, person.getId());
            logger.error(message, e);
            throw new DataMapperException(message, e);
        }
    }

    @Override
    public void insert(Person person) throws DataMapperException {

        try (Connection connection = ConnectionUtils.getConnection();
             var statementEmail = connection.prepareStatement(INSERTION_EMAILS_QUERY_TEMPLATE);
             var statementPerson = connection.prepareStatement(INSERTION_QUERY_TEMPLATE)) {

            connection.setAutoCommit(false);

            statementEmail.setString(INSERTION_EMAILS_EMAIL_PARAMETER, person.getEmail());
            statementEmail.addBatch();
            statementEmail.executeBatch();

            statementPerson.setInt(INSERTION_PERSON_ID_PARAMETER, person.getId());
            statementPerson.setString(INSERTION_FIRST_NAME_PARAMETER, person.getFirstName());
            statementPerson.setString(INSERTION_SECOND_NAME_PARAMETER, person.getLastName());
            statementPerson.addBatch();
            statementPerson.executeBatch();

            connection.commit();

        } catch (SQLException e) {
            var message = String.format(MESSAGE_TEMPLATE_CANNOT_SAVE_THE_PERSON, person.getId());
            logger.error(message, e);
            throw new DataMapperException(message, e);
        }

    }

    @Override
    public void delete(Person person) throws DataMapperException {

        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement personStatement = connection.prepareStatement(DELETION_QUERY_TEMPLATE);
             PreparedStatement emailStatement = connection.prepareStatement(DELETION_EMAILS_QUERY_TEMPLATE)) {

            connection.setAutoCommit(false);

            int emailId = findEmailIdByPersonId(person.getId()).orElseThrow();

            emailStatement.setInt(DELETION_EMAILS_EMAIL_ID_PARAMETER, emailId);
            emailStatement.addBatch();
            emailStatement.executeBatch();

            personStatement.setInt(DELETION_PERSON_ID_PARAMETER, person.getId());
            personStatement.addBatch();
            personStatement.executeBatch();

            connection.commit();

        } catch (SQLException e) {
            var message = String.format(MESSAGE_TEMPLATE_CANNOT_DELETE_THE_PERSON, person.getId());
            logger.error(message, e);
            throw new DataMapperException(message, e);
        }
    }

}
