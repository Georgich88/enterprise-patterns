package com.isaev.ee.lazyload.database.lazyload;

import com.isaev.ee.lazyload.database.exception.DataMapperException;
import com.isaev.ee.lazyload.people.Person;
import com.isaev.ee.lazyload.people.PersonMapper;

public class PersonHolder implements ValueHolder<Person> {

    private Person value;
    private PersonMapper dataMapper;

    public PersonHolder(PersonMapper dataMapper, int personId) throws DataMapperException {
        this.dataMapper = dataMapper;
        this.value = dataMapper.findByIdLazily(personId).orElseThrow();
    }

    @Override
    public Person getValue() {
        return this.value;
    }

    public String getEmail() throws DataMapperException {
        return dataMapper.findEmailByPersonId(value.getId()).orElseThrow();
    }

}
