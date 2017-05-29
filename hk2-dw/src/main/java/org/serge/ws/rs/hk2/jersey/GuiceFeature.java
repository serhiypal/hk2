package org.serge.ws.rs.hk2.jersey;

import javax.inject.Inject;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import java.util.Arrays;

import org.glassfish.hk2.api.ServiceLocator;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.jvnet.hk2.guice.bridge.api.HK2IntoGuiceBridge;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

@Provider
public class GuiceFeature implements Feature {

    public static final String MODULES = GuiceFeature.class.getName() + ".modules";

    private final ServiceLocator serviceLocator;

    @Inject
    public GuiceFeature(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Override
    public boolean configure(FeatureContext context) {
        Module[] configModules = (Module[]) context.getConfiguration().getProperty(MODULES);
        Module[] modules = new Module[configModules.length + 1];
        modules[0] = new HK2IntoGuiceBridge(serviceLocator);
        System.arraycopy(configModules, 0, modules, 1, configModules.length);

        Injector injector = Guice.createInjector(modules);
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge bridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        bridge.bridgeGuiceInjector(injector);
        return true;
    }

}
