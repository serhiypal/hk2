package org.serge.ws.rs.hk2;

import java.util.stream.Stream;

import org.serge.ws.rs.hk2.guice.GuiceModule;
import org.serge.ws.rs.hk2.jersey.BridgeFeature;
import org.serge.ws.rs.hk2.jersey.GuiceFeature;
import org.serge.ws.rs.hk2.jersey.ScanningFeature;
import org.serge.ws.rs.hk2.jersey.SpringFeature;

import com.google.inject.Module;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class Hk2Application extends Application<HkConfiguration> {

    public void run(HkConfiguration hkConfiguration, Environment environment) throws Exception {
        environment.jersey().packages(getClass().getPackage().getName() + ".resource");
        environment.jersey().register(ScanningFeature.class);
        environment.jersey().property(SpringFeature.PACKAGES_PROPERTY, "org.serge.ws.rs.hk2.spring");
        environment.jersey().register(SpringFeature.class);
        environment.jersey().property(GuiceFeature.MODULES, Stream.<Module>of(new GuiceModule()).toArray(Module[]::new));
        environment.jersey().register(GuiceFeature.class);
    }

    public static void main(String[] args) {
        try {
            new Hk2Application().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
