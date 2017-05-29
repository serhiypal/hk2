package org.serge.ws.rs.hk2;

import org.serge.ws.rs.hk2.jersey.SpringFeature;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class Hk2Application extends Application<HkConfiguration> {

    public void run(HkConfiguration hkConfiguration, Environment environment) throws Exception {
        environment.jersey().packages(getClass().getPackage().getName());
        environment.jersey().property(SpringFeature.PACKAGES_PROPERTY, "org.serge.ws.rs.hk2.spring");
    }

    public static void main(String[] args) {
        try {
            new Hk2Application().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
