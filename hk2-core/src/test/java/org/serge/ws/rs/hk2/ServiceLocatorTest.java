package org.serge.ws.rs.hk2;

import java.util.List;

import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.extras.ExtrasUtilities;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Test;
import org.serge.ws.rs.hk2.locator.nesting.SomeContract;
import org.serge.ws.rs.hk2.locator.nesting.SomeService1;
import org.serge.ws.rs.hk2.locator.nesting.SomeService2;
import org.serge.ws.rs.hk2.locator.nesting.SomeService3;
import org.serge.ws.rs.hk2.locator.nesting.SomeService4;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ServiceLocatorTest {

    @Test
    public void nesting() {
        ServiceLocator parentLocator = ServiceLocatorFactory.getInstance().create("parent");
        ServiceLocator childLocator1 = ServiceLocatorFactory.getInstance().create("child1", parentLocator);
        ServiceLocator childLocator2 = ServiceLocatorFactory.getInstance().create("child2", childLocator1);
        ServiceLocator childLocator3 = ServiceLocatorFactory.getInstance().create("child3", childLocator1);

        ServiceLocatorUtilities.addClasses(childLocator1, SomeService1.class);
        ServiceLocatorUtilities.addClasses(childLocator2, SomeService2.class);
        ServiceLocatorUtilities.addClasses(childLocator3, SomeService3.class);

        List<ServiceHandle<SomeContract>> parentServices = parentLocator.getAllServiceHandles(SomeContract.class);
        assertThat(parentServices.size(), is(0));
        assertThat(parentServices.get(0).getActiveDescriptor().getImplementationClass(), equalTo(SomeService1.class));

        List<ServiceHandle<SomeContract>> childServices1 = childLocator1.getAllServiceHandles(SomeContract.class);
        assertThat(childServices1.size(), is(1));

        List<ServiceHandle<SomeContract>> childServices2 = childLocator2.getAllServiceHandles(SomeContract.class);
        assertThat(childServices2.size(), is(2));

        List<ServiceHandle<SomeContract>> childServices3 = childLocator3.getAllServiceHandles(SomeContract.class);
        assertThat(childServices3.size(), is(2));

        ServiceLocatorFactory.getInstance().destroy(parentLocator);
        ServiceLocatorFactory.getInstance().destroy(childLocator1);
        ServiceLocatorFactory.getInstance().destroy(childLocator2);
        ServiceLocatorFactory.getInstance().destroy(childLocator3);
    }

    @Test
    public void bridging() {
        ServiceLocator parentLocator = ServiceLocatorFactory.getInstance().create("parent");
        ServiceLocator childLocator1 = ServiceLocatorFactory.getInstance().create("child1");
        ServiceLocator childLocator2 = ServiceLocatorFactory.getInstance().create("child2");
        ServiceLocator childLocator3 = ServiceLocatorFactory.getInstance().create("child3");
        ServiceLocatorUtilities.addClasses(parentLocator, SomeService1.class);
        ServiceLocatorUtilities.addClasses(childLocator1, SomeService2.class);
        ServiceLocatorUtilities.addClasses(childLocator2, SomeService3.class);
        ServiceLocatorUtilities.addClasses(childLocator3, SomeService4.class);

        ExtrasUtilities.bridgeServiceLocator(parentLocator, childLocator1);
        ExtrasUtilities.bridgeServiceLocator(childLocator1, childLocator2);
        ExtrasUtilities.bridgeServiceLocator(childLocator1, childLocator3);

        List<ServiceHandle<SomeContract>> parentServices = parentLocator.getAllServiceHandles(SomeContract.class);
        assertThat(parentServices.size(), is(4));

        List<ServiceHandle<SomeContract>> childServices1 = childLocator1.getAllServiceHandles(SomeContract.class);
        assertThat(childServices1.size(), is(3));

        List<ServiceHandle<SomeContract>> childServices2 = childLocator2.getAllServiceHandles(SomeContract.class);
        assertThat(childServices2.size(), is(1));

        List<ServiceHandle<SomeContract>> childServices3 = childLocator3.getAllServiceHandles(SomeContract.class);
        assertThat(childServices3.size(), is(1));

        ServiceLocatorFactory.getInstance().destroy(parentLocator);
        ServiceLocatorFactory.getInstance().destroy(childLocator1);
        ServiceLocatorFactory.getInstance().destroy(childLocator2);
        ServiceLocatorFactory.getInstance().destroy(childLocator3);
    }

    @Test
    public void nestingEmpty() {
        ServiceLocator parentLocator = ServiceLocatorFactory.getInstance().create("parent");
        ServiceLocator childLocator1 = ServiceLocatorFactory.getInstance().create("child1", parentLocator);
        ServiceLocator childLocator2 = ServiceLocatorFactory.getInstance().create("child2", childLocator1);
        ServiceLocatorUtilities.addClasses(childLocator1, SomeService2.class);
        ServiceLocatorUtilities.addClasses(childLocator2, SomeService3.class);

        List<ServiceHandle<SomeContract>> parentServices = parentLocator.getAllServiceHandles(SomeContract.class);
        assertThat(parentServices.size(), is(0));

        List<ServiceHandle<SomeContract>> childServices1 = childLocator1.getAllServiceHandles(SomeContract.class);
        assertThat(childServices1.size(), is(1));

        List<ServiceHandle<SomeContract>> childServices2 = childLocator2.getAllServiceHandles(SomeContract.class);
        assertThat(childServices2.size(), is(2));

        ServiceLocatorFactory.getInstance().destroy(parentLocator);
        ServiceLocatorFactory.getInstance().destroy(childLocator1);
        ServiceLocatorFactory.getInstance().destroy(childLocator2);
    }

    @Test
    public void bridgingBidirection() {
        ServiceLocator parentLocator = ServiceLocatorFactory.getInstance().create("parent");
        ServiceLocator childLocator1 = ServiceLocatorFactory.getInstance().create("child1");
        ServiceLocatorUtilities.addClasses(parentLocator, SomeService1.class);
        ServiceLocatorUtilities.addClasses(childLocator1, SomeService2.class);

        ExtrasUtilities.bridgeServiceLocator(parentLocator, childLocator1);
        ExtrasUtilities.bridgeServiceLocator(childLocator1, parentLocator);

        List<ServiceHandle<SomeContract>> parentServices = parentLocator.getAllServiceHandles(SomeContract.class);
        assertThat(parentServices.size(), is(2));

        List<ServiceHandle<SomeContract>> childServices1 = childLocator1.getAllServiceHandles(SomeContract.class);
        assertThat(childServices1.size(), is(2));

        ServiceLocatorFactory.getInstance().destroy(parentLocator);
        ServiceLocatorFactory.getInstance().destroy(childLocator1);
    }

}
