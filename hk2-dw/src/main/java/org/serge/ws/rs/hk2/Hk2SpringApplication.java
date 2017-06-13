package org.serge.ws.rs.hk2;

import javax.inject.Singleton;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.serge.ws.rs.hk2.spring2.SpringBundle;
import org.serge.ws.rs.hk2.spring2.SpringConsumer;
import org.serge.ws.rs.hk2.spring2.SpringResource;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class Hk2SpringApplication extends Application<HkConfiguration> {

    public void run(HkConfiguration hkConfiguration, Environment environment) throws Exception {
        environment.jersey().register(SpringResource.class);
        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(SpringConsumer.class).to(SpringConsumer.class).in(Singleton.class);
            }
        });
    }

    @Override
    public void initialize(Bootstrap<HkConfiguration> bootstrap) {
        bootstrap.addBundle(new SpringBundle("org.serge.ws.rs.hk2.spring2"));
    }

    public static void main(String[] args) {
        try {
            new Hk2SpringApplication().run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
