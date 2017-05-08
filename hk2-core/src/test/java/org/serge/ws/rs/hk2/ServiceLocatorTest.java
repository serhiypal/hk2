package org.serge.ws.rs.hk2;

import java.util.List;

import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.extras.ExtrasUtilities;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Test;
import org.serge.ws.rs.hk2.locator.SomeContract;
import org.serge.ws.rs.hk2.locator.SomeService1;
import org.serge.ws.rs.hk2.locator.SomeService2;
import org.serge.ws.rs.hk2.locator.SomeService3;
import org.serge.ws.rs.hk2.locator.SomeService4;

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

        ServiceLocatorUtilities.addClasses(parentLocator, SomeService1.class);
        ServiceLocatorUtilities.addClasses(childLocator1, SomeService2.class);
        ServiceLocatorUtilities.addClasses(childLocator3, SomeService4.class);
        // Parent does not look into the chain just itself
        List<ServiceHandle<SomeContract>> parentServices = parentLocator.getAllServiceHandles(SomeContract.class);
        assertThat(parentServices.size(), is(1));
        assertThat(parentServices.get(0).getActiveDescriptor().getImplementationClass(), equalTo(SomeService1.class));
        // Child looks into itself then parent
        List<ServiceHandle<SomeContract>> childServices1 = childLocator1.getAllServiceHandles(SomeContract.class);
        assertThat(childServices1.size(), is(2));
        // Siblings do not know about each other
        List<ServiceHandle<SomeContract>> childServices2 = childLocator2.getAllServiceHandles(SomeContract.class);
        assertThat(childServices2.size(), is(2));

        List<ServiceHandle<SomeContract>> childServices3 = childLocator3.getAllServiceHandles(SomeContract.class);
        assertThat(childServices3.size(), is(3));
        // Destroying does not break the chain
        childLocator1.shutdown();
        ServiceLocatorFactory.getInstance().destroy(childLocator1);
        // Destroying parent won't affect the container
        childServices2 = childLocator2.getAllServiceHandles(SomeContract.class);
        assertThat(childServices2.size(), is(2));
        // Destroy all locators
        ServiceLocatorFactory.getInstance().destroy(parentLocator);
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
        // Bridging looks through the chain, so the top most can see all the service going down
        List<ServiceHandle<SomeContract>> parentServices = parentLocator.getAllServiceHandles(SomeContract.class);
        assertThat(parentServices.size(), is(4));
        // Same as above but does not look into the parent, just down (unidirectional)
        List<ServiceHandle<SomeContract>> childServices1 = childLocator1.getAllServiceHandles(SomeContract.class);
        assertThat(childServices1.size(), is(3));
        // As with nesting, siblings do not know about each other just top-down unless there is a bridge
        List<ServiceHandle<SomeContract>> childServices2 = childLocator2.getAllServiceHandles(SomeContract.class);
        assertThat(childServices2.size(), is(1));
        // Cannot see sibling, and no child just itself
        List<ServiceHandle<SomeContract>> childServices3 = childLocator3.getAllServiceHandles(SomeContract.class);
        assertThat(childServices3.size(), is(1));
        // We can unbridge locators thus the link between breaks as it would be two separate locators
        ExtrasUtilities.unbridgeServiceLocator(parentLocator, childLocator1);
        // After breaking parent has just have 1 service it contains
        parentServices = parentLocator.getAllServiceHandles(SomeContract.class);
        assertThat(parentServices.size(), is(1));
        // Since child does not look up it still has 3 services pull from itself and bridged/child service locators
        childServices1 = childLocator1.getAllServiceHandles(SomeContract.class);
        assertThat(childServices1.size(), is(3));

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

    @Test
    public void bridgingBidirectionalMulti() {
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
        ExtrasUtilities.bridgeServiceLocator(childLocator3, parentLocator);
        // Bridging looks through the chain, so the top most can see all the service going down
        List<ServiceHandle<SomeContract>> parentServices = parentLocator.getAllServiceHandles(SomeContract.class);
        assertThat(parentServices.size(), is(4));
        // Same as above but does not look into the parent, just down (unidirectional)
        List<ServiceHandle<SomeContract>> childServices1 = childLocator1.getAllServiceHandles(SomeContract.class);
        assertThat(childServices1.size(), is(4));
        // As with nesting, siblings do not know about each other just top-down unless there is a bridge
        List<ServiceHandle<SomeContract>> childServices2 = childLocator2.getAllServiceHandles(SomeContract.class);
        assertThat(childServices2.size(), is(1));
        // Cannot see sibling, and no child just itself
        List<ServiceHandle<SomeContract>> childServices3 = childLocator3.getAllServiceHandles(SomeContract.class);
        assertThat(childServices3.size(), is(4));
        // We can unbridge locators thus the link between breaks as it would be two separate locators
        ExtrasUtilities.unbridgeServiceLocator(parentLocator, childLocator1);
        // After breaking parent has just have 1 service it contains
        parentServices = parentLocator.getAllServiceHandles(SomeContract.class);
        assertThat(parentServices.size(), is(1));
        // Since child does not look up it still has 3 services pull from itself and bridged/child service locators
        childServices1 = childLocator1.getAllServiceHandles(SomeContract.class);
        assertThat(childServices1.size(), is(4));

        ServiceLocatorFactory.getInstance().destroy(parentLocator);
        ServiceLocatorFactory.getInstance().destroy(childLocator1);
        ServiceLocatorFactory.getInstance().destroy(childLocator2);
        ServiceLocatorFactory.getInstance().destroy(childLocator3);
    }

}
