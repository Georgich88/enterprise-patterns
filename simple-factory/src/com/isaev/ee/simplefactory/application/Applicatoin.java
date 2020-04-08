package com.isaev.ee.simplefactory.application;

import com.isaev.ee.simplefactory.namefactory.NameFactory;

public class Applicatoin {

    public static void main(String[] args) {
        var factory = new NameFactory();
        var first = factory.getName("first");
        var second = factory.getName("first");
    }
}
