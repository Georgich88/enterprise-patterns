package com.isaev.ee.serializedlob.organization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores information about customer departments.
 *
 * @author Georgy Isaev
 */
public class Department {

    private final List<Department> subsidiaries = new ArrayList<>();
    private String name;

    public Department(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Department> getSubsidiaries() {
        return Collections.unmodifiableList(subsidiaries);
    }

    public void addSubsidiary(String name) {
        Department subsidiary = new Department(name);
        this.subsidiaries.add(subsidiary);
    }

    public void addSubsidiary(Department subsidiary) {
        this.subsidiaries.add(subsidiary);
    }
}
