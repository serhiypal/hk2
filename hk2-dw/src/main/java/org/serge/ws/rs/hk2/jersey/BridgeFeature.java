package org.serge.ws.rs.hk2.jersey;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.extras.ExtrasUtilities;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;

@Provider
@Priority(1)
public class BridgeFeature implements Feature {

    private final ServiceLocator locator;

    @Inject
    public BridgeFeature(ServiceLocator serviceLocator) {
        this.locator = serviceLocator;
    }

    @Override
    public boolean configure(FeatureContext context) {
        ServiceLocator bridgeLocator = ServiceLocatorUtilities.createAndPopulateServiceLocator();
        ExtrasUtilities.bridgeServiceLocator(locator, bridgeLocator);
        ExtrasUtilities.bridgeServiceLocator(bridgeLocator, locator);
        return true;
    }
}
