package org.serge.ws.rs.hk2;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.extras.ExtrasUtilities;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Test;
import org.serge.ws.rs.hk2.event.Publisher;
import org.serge.ws.rs.hk2.event.Subscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EventTest {

    @Test
    public void listen() throws InterruptedException {
        ServiceLocator locator = ServiceLocatorFactory.getInstance().create("events");
        ExtrasUtilities.enableTopicDistribution(locator);
        ServiceLocatorUtilities.addClasses(locator, Publisher.class, Subscriber.class);
        Publisher publisher = locator.getService(Publisher.class);
        Subscriber subscriber = locator.getService(Subscriber.class);

        String message = "Event message";
        publisher.send(message);
        assertThat(subscriber.getMessage(), is(message));
    }

}
