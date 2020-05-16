package com.isaev.ee.cacheaccessor.cache;

import com.isaev.ee.cacheaccessor.dataaccess.DataAccessible;
import com.isaev.ee.cacheaccessor.domain.Person;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CacheAccessor implements DataAccessible {

    private final Set<Person> firstPeopleCache = new HashSet<>();
    private final Set<Person> lastPeopleCache = new HashSet<>();

    private final DataAccessible<Person> dataAccessor;

    public CacheAccessor(DataAccessible<Person> dataAccessor) {
        this.dataAccessor = dataAccessor;
    }

    @Override
    public Optional<Person> findFirst() {
        if (firstPeopleCache.isEmpty()) {
            firstPeopleCache.add(this.dataAccessor.findFirst().orElseThrow());
        }
        return Optional.of(firstPeopleCache.iterator().next());
    }

    @Override
    public Optional<Person> findLast() {
        if (lastPeopleCache.isEmpty()) {
            lastPeopleCache.add(this.dataAccessor.findLast().orElseThrow());
        }
        return Optional.of(lastPeopleCache.iterator().next());
    }

    public Set<Person> getFirstPeopleCache() {
        return Collections.unmodifiableSet(firstPeopleCache);
    }

    public Set<Person> getLastPeopleCache() {
        return Collections.unmodifiableSet(lastPeopleCache);
    }
}
