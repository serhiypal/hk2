package org.serge.ws.rs.hk2.factory;

import java.util.HashMap;
import java.util.Map;

import org.jvnet.hk2.annotations.Service;

@Service
public class DistributedCache implements Cache {

    private Map<Object, Object> cache = new HashMap<>();

    public Object get(Object key) {
        System.out.println("Distributed get: " + key);
        return cache.get(key);
    }

    public Object put(Object key, Object value) {
        System.out.println("Distributed put: key=" + key + " value=" + value);
        return cache.put(key, value);
    }

    public Object remove(Object key) {
        System.out.println("Distributed remove: " + key);
        return cache.remove(key);
    }

    public void removeAll() {
        System.out.println("Distributed remove all");
        cache.clear();
    }

}
