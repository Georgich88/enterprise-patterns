package com.isaev.ee.serializedlob.people;

import com.isaev.ee.serializedlob.organization.Department;

import java.util.StringJoiner;

/**
 * Stores customer details.
 *
 * @author Georgy Isaev
 */
public class Customer {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private Department department;

    // Constructors

    public Customer(int id, String firstName, String lastName, String email) {
        this(id, firstName, lastName, email, null);
    }

    public Customer(int id, String firstName, String lastName, String email, Department department) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    // Object inherited methods

    @Override
    public String toString() {
        return new StringJoiner(", ", Customer.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .add("email='" + email + "'")
                .add("department='" + department + "'")
                .toString();
    }
}
