package com.isaev.ee.cachecollector.fixedexpirationcache;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class FixedExpirationCacheEntryFactoryTest {

    @Test
    void shouldCollectCache(){
        assertDoesNotThrow(()->{
            var animalSpeedFactory = new FixedExpirationCacheEntryFactory<String, Integer>(9);
            animalSpeedFactory.createCacheEntry("horse", 88);
            animalSpeedFactory.createCacheEntry("leopard", 58);
            TimeUnit.SECONDS.sleep(10);
            animalSpeedFactory.createCacheEntry("cheetah", 93);
            animalSpeedFactory.collect();
            assertEquals(1, animalSpeedFactory.getCache().size());
        });
    }

}