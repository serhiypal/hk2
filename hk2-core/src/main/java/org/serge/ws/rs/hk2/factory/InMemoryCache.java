package org.serge.ws.rs.hk2.factory;

import java.util.HashMap;
import java.util.Map;

import org.jvnet.hk2.annotations.Service;

@Service
public class InMemoryCache implements Cache {

    private Map<Object, Object> cache = new HashMap<>();

    public Object get(Object key) {
        System.out.println("Inmem get: " + key);
        return cache.get(key);
    }

    public Object put(Object key, Object value) {
        System.out.println("Inmem put: key=" + key + " value=" + value);
        return cache.put(key, value);
    }

    public Object remove(Object key) {
        System.out.println("Inmem remove: " + key);
        return cache.remove(key);
    }

    public void removeAll() {
        System.out.println("Inmem remove all");
        cache.clear();
    }

}
