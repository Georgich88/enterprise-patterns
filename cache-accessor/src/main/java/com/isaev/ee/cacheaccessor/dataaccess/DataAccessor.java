package com.isaev.ee.cacheaccessor.dataaccess;

import com.isaev.ee.cacheaccessor.domain.Person;

import java.util.Optional;

public class DataAccessor implements DataAccessible {

    public DataAccessor() {
    }

    @Override
    public Optional<Person> findFirst() {
        return Optional.of(new Person("First", "Person"));
    }

    @Override
    public Optional findLast() {
        return Optional.of(new Person("Last", "Person"));
    }
}
