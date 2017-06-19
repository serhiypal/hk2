package org.serge.ws.rs.jersey.provider;

import javax.inject.Inject;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.Populator;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.ClasspathDescriptorFileFinder;

@Provider
public class ScanningFeature implements Feature {

    private final ServiceLocator locator;

    @Inject
    public ScanningFeature(ServiceLocator locator) {
        this.locator = locator;
    }

    @Override
    public boolean configure(FeatureContext context) {
        DynamicConfigurationService service = locator.getService(DynamicConfigurationService.class);
        Populator populator = service.getPopulator();
        try {
            populator.populate(new ClasspathDescriptorFileFinder(getClass().getClassLoader()));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

}
