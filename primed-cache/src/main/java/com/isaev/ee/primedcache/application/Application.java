package com.isaev.ee.primedcache.application;

import com.isaev.ee.primedcache.cache.CacheAccessor;
import com.isaev.ee.primedcache.dataaccess.DataAccessible;
import com.isaev.ee.primedcache.dataaccess.DataAccessor;
import com.isaev.ee.primedcache.domain.AuthorizationData;
import com.isaev.ee.primedcache.key.AuthorizationKeyFactory;
import com.isaev.ee.primedcache.key.KeyFactory;

public class Application {

    public static void main(String[] args) {

        KeyFactory<String, String, AuthorizationData> factory = new AuthorizationKeyFactory();
        DataAccessible<String, String, AuthorizationData> dataAccessor = new CacheAccessor(new DataAccessor());

        var rossKey = factory.createKey(new AuthorizationData("Ross", "Support"));
        rossKey.addKey("Country", "US");

        var ross = dataAccessor.read(rossKey);
        System.out.println(ross);


    }

}
