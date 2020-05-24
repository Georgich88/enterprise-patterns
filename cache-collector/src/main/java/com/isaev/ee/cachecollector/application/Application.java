package com.isaev.ee.cachecollector.application;

import com.isaev.ee.cachecollector.fixedexpirationcache.FixedExpirationCacheEntryFactory;

import java.util.concurrent.TimeUnit;

public class Application {

    public static final int DEFAULT_EXPIRATION_INTERVAL = 10;

    public static void main(String[] args) throws Exception {
        var animalSpeedFactory = new FixedExpirationCacheEntryFactory<String, Integer>(DEFAULT_EXPIRATION_INTERVAL);
        animalSpeedFactory.createCacheEntry("horse", 88);
        animalSpeedFactory.createCacheEntry("leopard", 58);
        TimeUnit.SECONDS.sleep(DEFAULT_EXPIRATION_INTERVAL);
        animalSpeedFactory.createCacheEntry("cheetah", 93);
        animalSpeedFactory.collect();
        System.out.println(animalSpeedFactory.getCache());

    }
}
