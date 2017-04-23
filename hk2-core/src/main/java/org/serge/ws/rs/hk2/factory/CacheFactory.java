package org.serge.ws.rs.hk2.factory;

import javax.inject.Singleton;

import org.glassfish.hk2.api.Factory;
import org.jvnet.hk2.annotations.Service;

@Service
public class CacheFactory implements Factory<Cache> {

    @Singleton
    @Override
    public Cache provide() {
        if ("distributed".equals(System.getProperty("cache"))) {
            return new DistributedCache();
        }
        return new InMemoryCache();
    }

    @Override
    public void dispose(Cache instance) {
        instance.removeAll();
    }

}
