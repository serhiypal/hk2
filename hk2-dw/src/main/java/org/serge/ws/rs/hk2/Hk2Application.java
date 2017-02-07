package org.serge.ws.rs.hk2;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class Hk2Application extends Application<HkConfiguration> {

    public void run(HkConfiguration hkConfiguration, Environment environment) throws Exception {
    }

    public static void main(String[] args) {
        try {
            new Hk2Application().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
