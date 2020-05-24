package com.isaev.ee.primedcache.key;

import com.isaev.ee.primedcache.domain.AuthorizationData;

public class AuthorizationKeyFactory implements KeyFactory<String, String, AuthorizationData> {
    @Override
    public Key<String, String> createKey(AuthorizationData domainObject) {

        Key<String, String> key = new AuthorizationKey();
        key.addKey("UserName", domainObject.getUserName());
        key.addKey("PageIdentifier", domainObject.getPageIdentifier());

        return key;
    }
}
