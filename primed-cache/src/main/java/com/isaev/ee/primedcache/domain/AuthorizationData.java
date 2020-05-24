package com.isaev.ee.primedcache.domain;

import java.util.StringJoiner;

public class AuthorizationData {

    private final String userName;
    private final String pageIdentifier;

    public AuthorizationData(String userName, String pageIdentifier) {
        this.userName = userName;
        this.pageIdentifier = pageIdentifier;
    }

    public String getUserName() {
        return userName;
    }

    public String getPageIdentifier() {
        return pageIdentifier;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AuthorizationData.class.getSimpleName() + "[", "]")
                .add("userName='" + userName + "'")
                .add("pageIdentifier='" + pageIdentifier + "'")
                .toString();
    }
}
