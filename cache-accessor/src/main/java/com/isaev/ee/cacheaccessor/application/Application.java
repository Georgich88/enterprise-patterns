package com.isaev.ee.cacheaccessor.application;

import com.isaev.ee.cacheaccessor.cache.CashAccessor;
import com.isaev.ee.cacheaccessor.dataaccess.DataAccessible;
import com.isaev.ee.cacheaccessor.dataaccess.DataAccessor;
import com.isaev.ee.cacheaccessor.domain.Person;

public class Application {
    public static void main(String[] args) {
        DataAccessible<Person> accessor = new CashAccessor(new DataAccessor());
        accessor.findFirst().ifPresent(System.out::println);
        accessor.findLast().ifPresent(System.out::println);
    }
}
