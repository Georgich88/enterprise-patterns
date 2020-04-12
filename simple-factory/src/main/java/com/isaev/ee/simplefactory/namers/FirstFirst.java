package com.isaev.ee.simplefactory.namers;

import java.util.StringJoiner;

public class FirstFirst extends Namer {

    public FirstFirst(String name) {
        super.frName = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FirstFirst.class.getSimpleName() + "[", "]")
                .add("frName='" + frName + "'")
                .toString();
    }
}
