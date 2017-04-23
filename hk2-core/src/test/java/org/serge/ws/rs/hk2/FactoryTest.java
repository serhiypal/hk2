package org.serge.ws.rs.hk2;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Test;
import org.serge.ws.rs.hk2.factory.CachableService;
import org.serge.ws.rs.hk2.factory.CachableServiceImpl;
import org.serge.ws.rs.hk2.factory.Cache;
import org.serge.ws.rs.hk2.factory.CacheFactory;
import org.serge.ws.rs.hk2.factory.DistributedCache;
import org.serge.ws.rs.hk2.factory.InMemoryCache;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FactoryTest {

    @Test
    public void factoryInmem() {
        System.clearProperty("cache");
        ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("hk2");
        ServiceLocatorUtilities.addClasses(serviceLocator, CacheFactory.class, CachableServiceImpl.class);
        CachableService cachableService = serviceLocator.getService(CachableService.class);
        cachableService.doSomething();

        Cache cache = serviceLocator.getService(Cache.class);
        assertThat(cache.get("Something in key"), is("Something in value"));
        assertThat(cache, instanceOf(InMemoryCache.class));

        serviceLocator.shutdown();
    }

    @Test
    public void factoryDistributed() {
        System.setProperty("cache", "distributed");
        ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("hk2");
        ServiceLocatorUtilities.addClasses(serviceLocator, CacheFactory.class, CachableServiceImpl.class);
        CachableService cachableService = serviceLocator.getService(CachableService.class);
        cachableService.doSomething();

        Cache cache = serviceLocator.getService(Cache.class);
        assertThat(cache.get("Something in key"), is("Something in value"));
        assertThat(cache, instanceOf(DistributedCache.class));

        serviceLocator.shutdown();
    }

}
