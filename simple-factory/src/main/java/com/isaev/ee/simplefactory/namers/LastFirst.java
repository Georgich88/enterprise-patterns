package com.isaev.ee.simplefactory.namers;

import java.util.StringJoiner;

public class LastFirst extends Namer {

    public LastFirst(String name) {
        super.lName = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LastFirst.class.getSimpleName() + "[", "]")
                .add("lName='" + lName + "'")
                .toString();
    }
}
