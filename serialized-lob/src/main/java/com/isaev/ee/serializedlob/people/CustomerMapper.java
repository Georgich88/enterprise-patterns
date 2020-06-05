package com.isaev.ee.serializedlob.people;

import com.isaev.ee.serializedlob.database.converters.DepartmentToXml;
import com.isaev.ee.serializedlob.database.converters.XmlToDepartment;
import com.isaev.ee.serializedlob.database.datamapper.DataMapper;
import com.isaev.ee.serializedlob.database.exception.DataMapperException;
import com.isaev.ee.serializedlob.database.utils.ConnectionUtils;
import org.apache.log4j.Logger;

import javax.xml.parsers.ParserConfigurationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.Optional;

public class CustomerMapper implements DataMapper<Customer> {

    public static final String MESSAGE_TEMPLATE_CANNOT_SAVE_THE_CUSTOMER = "Cannot save the customer with ID: %s";
    public static final String MESSAGE_TEMPLATE_CANNOT_DELETE_THE_CUSTOMER = "Cannot remove the customer with ID: %s";
    private static final String INSERTION_EMAILS_QUERY_TEMPLATE = "INSERT INTO public.emails(email_id, email) VALUES (nextval('emails_id_seq'), ?);";
    private static final int INSERTION_EMAILS_EMAIL_PARAMETER = 1;

    private static final String INSERTION_QUERY_TEMPLATE = "INSERT INTO public.customers(customer_id, first_name, second_name, email_id, department) VALUES (?, ?, ?, currval('emails_id_seq'), ?);";
    private static final String INSERTION_QUERY_WITH_DEPARTMENT_TEMPLATE = "INSERT INTO public.customers(customer_id, first_name, second_name, email_id, department) VALUES (?, ?, ?, currval('emails_id_seq'), XMLPARSE(DOCUMENT ?));";
    private static final int INSERTION_CUSTOMER_ID_PARAMETER = 1;
    private static final int INSERTION_FIRST_NAME_PARAMETER = 2;
    private static final int INSERTION_SECOND_NAME_PARAMETER = 3;
    private static final int INSERTION_DEPARTMENT_PARAMETER = 4;
    private static final String UPDATING_EMAILS_QUERY_TEMPLATE = "UPDATE emails SET email = ? WHERE email_id = ?;";
    private static final int UPDATING_EMAILS_EMAIL_PARAMETER = 1;
    private static final int UPDATING_EMAILS_EMAIL_ID_PARAMETER = 2;

    private static final String UPDATING_QUERY_TEMPLATE = "UPDATE customers SET first_name = ?, second_name = ?, email_id = ?, department = ? WHERE customer_id = ?";
    private static final String UPDATING_QUERY_WITH_DEPARTMENT_TEMPLATE = "UPDATE customers SET first_name = ?, second_name = ?, email_id = ?, department = XMLPARSE(DOCUMENT ?) WHERE customer_id = ?";
    private static final int UPDATING_FIRST_NAME_PARAMETER = 1;
    private static final int UPDATING_SECOND_NAME_PARAMETER = 2;
    private static final int UPDATING_EMAIL_ID_PARAMETER = 3;
    private static final int UPDATING_DEPARTMENT_PARAMETER = 4;
    private static final int UPDATING_CUSTOMER_ID_PARAMETER = 5;
    private static final String DELETION_QUERY_TEMPLATE = "DELETE FROM public.customers  WHERE customer_id = ?";
    private static final int DELETION_CUSTOMER_ID_PARAMETER = 1;
    private static final String DELETION_EMAILS_QUERY_TEMPLATE = "DELETE FROM public.emails  WHERE email_id = ?";
    private static final int DELETION_EMAILS_EMAIL_ID_PARAMETER = 1;

    private static final String SELECTION_BY_ID_QUERY_TEMPLATE =
            "SELECT customers.customer_id, " +
                    "customers.first_name, " +
                    "customers.second_name, " +
                    "emails.email, " +
                    "customers.department " +
                    "FROM customers " +
                    "LEFT JOIN emails " +
                    "ON customers.email_id = emails.email_id " +
                    "WHERE customers.customer_id = ?";
    private static final int SELECTION_BY_ID_CUSTOMER_ID_PARAMETER = 1;
    private static final String FIRST_NAME_FIELD_NAME = "first_name";
    private static final String SECOND_NAME_FIELD_NAME = "second_name";
    private static final String EMAIL_FIELD_NAME = "email";
    private static final String DEPARTMENT_FIELD_NAME = "department";
    private static final String EMAIL_ID_FIELD_NAME = "email_id";

    private static final String SELECTION_EMAIL_ID_BY_CUSTOMER_ID_QUERY_TEMPLATE =
            "SELECT customers.email_id " +
                    "FROM customers " +
                    "WHERE customers.customer_id = ?";
    private static final int SELECTION_EMAIL_ID_BY_CUSTOMER_ID_PARAMETER = 1;
    private static final String MESSAGE_TEMPLATE_CANNOT_FIND_CUSTOMER = "Cannot find customer by ID: %s";
    private final static Logger logger = Logger.getLogger(CustomerMapper.class);

    // Constructors

    public CustomerMapper() {
    }

    // Database operation logic

    public static Optional<Integer> findEmailIdByCustomerId(int customerId) throws DataMapperException {

        Optional<Integer> emailId;

        try (Connection connection = ConnectionUtils.getConnection();
             var statement = connection.prepareStatement(SELECTION_EMAIL_ID_BY_CUSTOMER_ID_QUERY_TEMPLATE)) {

            statement.setInt(SELECTION_EMAIL_ID_BY_CUSTOMER_ID_PARAMETER, customerId);
            var resultSet = statement.executeQuery();
            resultSet.next();

            emailId = Optional.of(resultSet.getInt(EMAIL_ID_FIELD_NAME));

        } catch (SQLException e) {
            var message = String.format(MESSAGE_TEMPLATE_CANNOT_FIND_CUSTOMER, customerId);
            logger.error(message, e);
            throw new DataMapperException(message, e);
        }

        return emailId;

    }


    public static Optional<Customer> findById(int customerId) throws DataMapperException {

        Optional<Customer> customer;

        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECTION_BY_ID_QUERY_TEMPLATE)) {
            statement.setInt(SELECTION_BY_ID_CUSTOMER_ID_PARAMETER, customerId);

            var resultSet = statement.executeQuery();
            resultSet.next();

            String firstName = resultSet.getString(FIRST_NAME_FIELD_NAME);
            String secondName = resultSet.getString(SECOND_NAME_FIELD_NAME);
            String email = resultSet.getString(EMAIL_FIELD_NAME);
            String department = resultSet.getString(DEPARTMENT_FIELD_NAME);

            customer = Optional.of(new Customer(customerId, firstName, secondName, email, new XmlToDepartment(department).toDepartment()));

        } catch (SQLException e) {
            var message = String.format(MESSAGE_TEMPLATE_CANNOT_FIND_CUSTOMER, customerId);
            logger.error(message, e);
            throw new DataMapperException(message, e);
        }

        return customer;

    }

    @Override
    public void update(Customer customer) throws DataMapperException {

        try (Connection connection = ConnectionUtils.getConnection();
             var customerStatement = connection.prepareStatement(getUpdatingQueryTemplate(customer));
             var emailStatement = connection.prepareStatement(UPDATING_EMAILS_QUERY_TEMPLATE)) {

            connection.setAutoCommit(false);

            int emailId = findEmailIdByCustomerId(customer.getId()).orElseThrow();

            emailStatement.setString(UPDATING_EMAILS_EMAIL_PARAMETER, customer.getEmail());
            emailStatement.setInt(UPDATING_EMAILS_EMAIL_ID_PARAMETER, emailId);
            emailStatement.addBatch();
            emailStatement.executeBatch();

            customerStatement.setInt(UPDATING_CUSTOMER_ID_PARAMETER, customer.getId());
            customerStatement.setString(UPDATING_FIRST_NAME_PARAMETER, customer.getFirstName());
            customerStatement.setString(UPDATING_SECOND_NAME_PARAMETER, customer.getLastName());

            if (customer.getDepartment() != null) {
                customerStatement.setString(UPDATING_DEPARTMENT_PARAMETER, new DepartmentToXml(customer.getDepartment()).toXmlString());
            } else {
                customerStatement.setNull(UPDATING_DEPARTMENT_PARAMETER, Types.SQLXML);
            }

            customerStatement.setInt(UPDATING_EMAIL_ID_PARAMETER, emailId);
            customerStatement.addBatch();
            customerStatement.executeBatch();

            connection.commit();

        } catch (SQLException | ParserConfigurationException e) {
            var message = String.format(MESSAGE_TEMPLATE_CANNOT_SAVE_THE_CUSTOMER, customer.getId());
            logger.error(message, e);
            throw new DataMapperException(message, e);
        }
    }

    @Override
    public void insert(Customer customer) throws DataMapperException {

        try (Connection connection = ConnectionUtils.getConnection();
             var statementEmail = connection.prepareStatement(INSERTION_EMAILS_QUERY_TEMPLATE);
             var statementCustomer = connection.prepareStatement(getInsertionQueryTemplate(customer))) {

            connection.setAutoCommit(false);

            statementEmail.setString(INSERTION_EMAILS_EMAIL_PARAMETER, customer.getEmail());
            statementEmail.addBatch();
            statementEmail.executeBatch();

            statementCustomer.setInt(INSERTION_CUSTOMER_ID_PARAMETER, customer.getId());
            statementCustomer.setString(INSERTION_FIRST_NAME_PARAMETER, customer.getFirstName());
            statementCustomer.setString(INSERTION_SECOND_NAME_PARAMETER, customer.getLastName());

            if (customer.getDepartment() != null) {
                statementCustomer.setObject(INSERTION_DEPARTMENT_PARAMETER, new DepartmentToXml(customer.getDepartment()).toXmlString());
            } else {
                statementCustomer.setNull(INSERTION_DEPARTMENT_PARAMETER, Types.SQLXML);
            }

            statementCustomer.addBatch();
            statementCustomer.executeBatch();

            connection.commit();

        } catch (SQLException | ParserConfigurationException e) {
            var message = String.format(MESSAGE_TEMPLATE_CANNOT_SAVE_THE_CUSTOMER, customer.getId());
            logger.error(message, e);
            throw new DataMapperException(message, e);
        }

    }

    @Override
    public void delete(Customer customer) throws DataMapperException {

        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement customerStatement = connection.prepareStatement(DELETION_QUERY_TEMPLATE);
             PreparedStatement emailStatement = connection.prepareStatement(DELETION_EMAILS_QUERY_TEMPLATE)) {

            connection.setAutoCommit(false);

            int emailId = findEmailIdByCustomerId(customer.getId()).orElseThrow();

            emailStatement.setInt(DELETION_EMAILS_EMAIL_ID_PARAMETER, emailId);
            emailStatement.addBatch();
            emailStatement.executeBatch();

            customerStatement.setInt(DELETION_CUSTOMER_ID_PARAMETER, customer.getId());
            customerStatement.addBatch();
            customerStatement.executeBatch();

            connection.commit();

        } catch (SQLException e) {
            var message = String.format(MESSAGE_TEMPLATE_CANNOT_DELETE_THE_CUSTOMER, customer.getId());
            logger.error(message, e);
            throw new DataMapperException(message, e);
        }
    }

    private static String getInsertionQueryTemplate(Customer customer) {
        if (customer == null && Objects.requireNonNull(customer).getDepartment() == null) {
            return INSERTION_QUERY_TEMPLATE;
        } else {
            return INSERTION_QUERY_WITH_DEPARTMENT_TEMPLATE;
        }
    }

    private static String getUpdatingQueryTemplate(Customer customer) {
        if (customer == null && Objects.requireNonNull(customer).getDepartment() == null) {
            return UPDATING_QUERY_TEMPLATE;
        } else {
            return UPDATING_QUERY_WITH_DEPARTMENT_TEMPLATE;
        }
    }

}
