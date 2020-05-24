package com.isaev.ee.primedcache.key;

import java.util.Map;

public interface Key<T, S> {

    /**
     * Determines if this instance of the key is a partial specification of the given key
     * For example, if specificKey contains (UserName='YAZAN', Region='UK', Page='Support')
     * and this key contains (UserName='YAZAN', Region='UK), then this key is partial of
     * specificKey
     *
     * @param specificKey - specific key to be examined.
     * @return true if the key is a partial specification of the specific key.
     */
    boolean isPartialOf(Key<T, S>  specificKey);

    Map<T, S> getKeys();

    void addKey(T attribute, S value);
}
