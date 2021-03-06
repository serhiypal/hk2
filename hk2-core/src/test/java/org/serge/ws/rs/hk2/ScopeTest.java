package org.serge.ws.rs.hk2;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.serge.ws.rs.hk2.scope.IntegerRandomService;
import org.serge.ws.rs.hk2.scope.RandomService;
import org.serge.ws.rs.hk2.scope.UUIDRandomService;

import static org.junit.Assert.assertEquals;

public class ScopeTest {

    private static ServiceLocator locator;

    @BeforeClass
    public static void setUp() {
        locator = ServiceLocatorFactory.getInstance().create("hk2");
        ServiceLocatorUtilities.enablePerThreadScope(locator);
        ServiceLocatorUtilities.addClasses(locator, UUIDRandomService.class, IntegerRandomService.class);
    }

    @AfterClass
    public static void tearDown() {
        locator.shutdown();
    }

    @Test
    public void randomLookup() {
        Set<Object> test1 = IntStream.rangeClosed(1, 3)
                                    .boxed()
                                    .map(i -> locator.getService(RandomService.class, "IntegerRandomService").random())
                                    .collect(Collectors.toSet());
        assertEquals("Wrong size", 3, test1.size());
    }

    @Test
    public void randomSingleThread() {
        Set<Object> test2 = IntStream.rangeClosed(1, 3)
                                     .boxed()
                                     .map(i -> locator.getService(RandomService.class, "UUIDRandomService").random())
                                     .collect(Collectors.toSet());
        assertEquals("Wrong size", 1, test2.size());
    }

    @Test
    public void randomMultiThread() {
        Set<Object> test3 =
                IntStream.rangeClosed(1, 3)
                         .boxed()
                         .map(i -> Executors.newSingleThreadExecutor()
                                            .submit(() -> locator.getService(RandomService.class, "UUIDRandomService")
                                                                 .random()))
                         .map(this::getUninterruptibly)
                         .filter(Objects::nonNull)
                         .collect(Collectors.toSet());
        assertEquals("Wrong size", 3, test3.size());
    }

    private <T> T getUninterruptibly(Future<T> future) {
        try {
            return future.get();
        } catch (Exception e) {
            return null;
        }

    }

}
