package com.isaev.ee.simplefactory.namefactory;

import com.isaev.ee.simplefactory.namers.FirstFirst;
import com.isaev.ee.simplefactory.namers.LastFirst;
import com.isaev.ee.simplefactory.namers.Namer;

public class NameFactory {

    enum Name {

        FIRST_FIRST("first"), LAST_FIRST("last");

        private String name;

        Name(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public NameFactory() {
    }

    public Namer getName(String name) {

        if (name.equals(Name.FIRST_FIRST.getName()))
            return new FirstFirst("First name");
        if (name.equals(Name.LAST_FIRST.getName()))
            return new LastFirst("Last name");

        throw new IllegalArgumentException(String.format("Wrong name: %s", name));

    }
}
