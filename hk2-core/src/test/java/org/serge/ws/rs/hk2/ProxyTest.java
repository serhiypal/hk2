package org.serge.ws.rs.hk2;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Test;
import org.serge.ws.rs.hk2.proxy.PrimaryKeyGenerator;
import org.serge.ws.rs.hk2.proxy.UUIDRandomService;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProxyTest {

    @Test
    public void proxy() {
        ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("proxies");
        ServiceLocatorUtilities.addClasses(serviceLocator, PrimaryKeyGenerator.class, UUIDRandomService.class);
        ServiceLocatorUtilities.enablePerThreadScope(serviceLocator);

        PrimaryKeyGenerator generator = serviceLocator.getService(PrimaryKeyGenerator.class);
        int size = IntStream.of(1, 2)
                            .boxed()
                            .map(i -> Executors.newSingleThreadExecutor())
                            .map(e -> e.submit(generator::getKey))
                            .map(f -> {
                                try {
                                    return f.get();
                                } catch (InterruptedException | ExecutionException e) {
                                    return null;
                                }
                            })
                            .collect(Collectors.toSet())
                            .size();
        assertThat(size, is(2));
    }
}
