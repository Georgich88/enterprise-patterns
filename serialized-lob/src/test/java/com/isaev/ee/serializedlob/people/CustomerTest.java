package com.isaev.ee.serializedlob.people;

import com.isaev.ee.serializedlob.database.exception.DataMapperException;
import com.isaev.ee.serializedlob.database.utils.QueryExecutor;
import com.isaev.ee.serializedlob.organization.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private static final CustomerMapper CUSTOMER_MAPPER = new CustomerMapper();
    public static final String CUSTOMER_FIRST_NAME = "Jon";
    public static final String CUSTOMER_LAST_NAME = "Jones";
    public static final String CUSTOMER_EMAIL = "jon.jones@gmail.com";
    public static final String DEPARTMENT_NAME_SALES = "Sales";
    public static final String DEPARTMENT_NAME_SALES_IN_UKRAINE = "Sales in Ukraine";
    public static final String DEPARTMENT_NAME_SALES_IN_GERMANY = "Sales in Germany";

    @BeforeEach
    public void initiateDatabaseBeforeTests() {
        QueryExecutor.createTables();
    }

    @Test
    @Order(1)
    public void shouldSaveCustomer() {
        Customer customerJonJones = new Customer(0, CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME, CUSTOMER_EMAIL);
        Department department = new Department(DEPARTMENT_NAME_SALES);
        department.addSubsidiary(DEPARTMENT_NAME_SALES_IN_UKRAINE);
        department.addSubsidiary(DEPARTMENT_NAME_SALES_IN_GERMANY);
        customerJonJones.setDepartment(department);
        Customer finalCustomerJonJones = customerJonJones;
        assertDoesNotThrow(() -> CUSTOMER_MAPPER.insert(finalCustomerJonJones));
        customerJonJones = assertDoesNotThrow(() -> CustomerMapper.findById(0).orElseThrow());
        assertEquals(CUSTOMER_FIRST_NAME, customerJonJones.getFirstName());
        assertEquals(CUSTOMER_LAST_NAME, customerJonJones.getLastName());
        assertEquals(CUSTOMER_EMAIL, customerJonJones.getEmail());
        assertNotNull(customerJonJones.getDepartment());
        assertEquals(DEPARTMENT_NAME_SALES, customerJonJones.getDepartment().getName());
    }

    @Test
    @Order(2)
    public void shouldUpdateCustomer() {
        Customer customerJonJones = new Customer(0, CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME, CUSTOMER_EMAIL);
        Department department = new Department(DEPARTMENT_NAME_SALES);
        department.addSubsidiary(DEPARTMENT_NAME_SALES_IN_UKRAINE);
        department.addSubsidiary(DEPARTMENT_NAME_SALES_IN_GERMANY);
        customerJonJones.setDepartment(department);
        Customer finalCustomerJonJones = customerJonJones;
        assertDoesNotThrow(() -> CUSTOMER_MAPPER.insert(finalCustomerJonJones));
        customerJonJones.setEmail("jon.jones@yandex.ru");
        customerJonJones.setFirstName("Jonie");
        customerJonJones.setLastName("Bones");
        assertDoesNotThrow(() -> CUSTOMER_MAPPER.update(finalCustomerJonJones));
        customerJonJones = assertDoesNotThrow(() -> CustomerMapper.findById(0).orElseThrow());
        assertEquals("Jonie", customerJonJones.getFirstName());
        assertEquals("Bones", customerJonJones.getLastName());
        assertEquals("jon.jones@yandex.ru", customerJonJones.getEmail());
    }

    @Test
    @Order(3)
    public void shouldRemoveCustomer() {
        Customer customerJonJones = new Customer(0, CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME, CUSTOMER_EMAIL);
        Department department = new Department(DEPARTMENT_NAME_SALES);
        department.addSubsidiary(DEPARTMENT_NAME_SALES_IN_UKRAINE);
        department.addSubsidiary(DEPARTMENT_NAME_SALES_IN_GERMANY);
        customerJonJones.setDepartment(department);
        final Customer finalCustomerJonJones = customerJonJones;
        assertDoesNotThrow(() -> CUSTOMER_MAPPER.insert(finalCustomerJonJones));
        assertDoesNotThrow(() -> CUSTOMER_MAPPER.delete(finalCustomerJonJones));
        assertThrows(DataMapperException.class, () -> CustomerMapper.findById(0));

    }
}