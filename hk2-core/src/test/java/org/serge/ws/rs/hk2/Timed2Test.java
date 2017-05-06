package org.serge.ws.rs.hk2;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.extras.ExtrasUtilities;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.serge.ws.rs.hk2.aop2.SleepService;
import org.serge.ws.rs.hk2.aop2.TimedMethodInterceptor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Timed2Test {

    private PrintStream out;

    @Before
    public void setUp() {
        out = System.out;
    }

    @After
    public void tearDown() {
        System.setOut(out);
    }

    @Test
    public void sleep() {
        ByteArrayOutputStream newOut = setUpOut(); // Replace System.out

        ServiceLocator serviceLocator = ServiceLocatorFactory.getInstance().create("sleep");
        ExtrasUtilities.enableDefaultInterceptorServiceImplementation(serviceLocator);
        ServiceLocatorUtilities.addClasses(serviceLocator, TimedMethodInterceptor.class, SleepService.class);
        SleepService sleepService = serviceLocator.getService(SleepService.class);

        String construct = new String(newOut.toByteArray());
        out.println(construct);
        assertThat(construct.contains(SleepService.class.getName()), is(true));

        newOut = setUpOut();
        sleepService.sleep(1000);

        String method = new String(newOut.toByteArray());
        out.println(method);
        assertThat(method.contains(SleepService.class.getName()), is(true));

        ServiceLocatorFactory.getInstance().destroy(serviceLocator);
    }

    private static ByteArrayOutputStream setUpOut() {
        ByteArrayOutputStream newOut = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(newOut);
        System.setOut(stream);
        return newOut;
    }
}
