package com.isaev.ee.primedcache.dataaccess;

import com.isaev.ee.primedcache.domain.AuthorizationData;
import com.isaev.ee.primedcache.key.AuthorizationKeyFactory;
import com.isaev.ee.primedcache.key.Key;
import com.isaev.ee.primedcache.key.KeyFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DataAccessor implements DataAccessible<String, String, AuthorizationData> {

    private final Map<Key<String, String>, AuthorizationData> demoData = new ConcurrentHashMap<>();
    private final KeyFactory<String, String, AuthorizationData> keyFactory = new AuthorizationKeyFactory();

    public DataAccessor() {
        populateDemoData();
    }

    private void populateDemoData() {

        var ross = new AuthorizationData("Ross", "Support");
        demoData.put(keyFactory.createKey(ross), ross);

        var rachel = new AuthorizationData("Rachel", "Main");
        demoData.put(keyFactory.createKey(rachel), rachel);

        var monica = new AuthorizationData("Monica", "Main");
        demoData.put(keyFactory.createKey(monica), monica);

        var chandler = new AuthorizationData("Chandler", "Support");
        demoData.put(keyFactory.createKey(chandler), chandler);

        var joey = new AuthorizationData("Joey", "Main");
        demoData.put(keyFactory.createKey(joey), joey);

        var phoebe = new AuthorizationData("Phoebe", "Support");
        demoData.put(keyFactory.createKey(phoebe), phoebe);
    }


    @Override
    public Optional<AuthorizationData> read(Key<String, String> specificKey) {
        if (demoData.containsKey(specificKey)) {
            return Optional.of(demoData.get(specificKey));
        }
        for (var entry : this.demoData.entrySet()){
            if (entry.getKey().isPartialOf(specificKey)){
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
    }
}
