package org.serge.ws.rs.hk2;

import java.io.IOException;
import java.util.Collections;

import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.Populator;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.junit.Test;
import org.serge.ws.rs.hk2.factory.Cache;
import org.serge.ws.rs.hk2.factory.InMemoryCache;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class GeneratorTest {

    @Test
    public void cache() throws IOException {
        ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("custom");
        DynamicConfigurationService dcs = serviceLocator.getService(DynamicConfigurationService.class);
        Populator populator = dcs.getPopulator();
        populator.populate(() -> Collections.singletonList(GeneratorTest.class.getResourceAsStream("/META-INF/hk2-locator/custom")));
        Cache cache = serviceLocator.getService(Cache.class);
        assertNotNull(cache);
        assertThat(cache, instanceOf(InMemoryCache.class));
    }

}
