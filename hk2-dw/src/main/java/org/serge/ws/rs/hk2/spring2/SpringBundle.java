package org.serge.ws.rs.hk2.spring2;

import java.util.Arrays;

import org.serge.ws.rs.hk2.jersey.SpringFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class SpringBundle implements Bundle {

    private static final Logger log = LoggerFactory.getLogger(SpringFeature.class);

    private final String[] scanPackages;

    public SpringBundle(String... scanPackages) {
        this.scanPackages = scanPackages;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) { }

    @Override
    public void run(Environment environment) {
        if (scanPackages == null || scanPackages.length == 0) {
            log.info("Packages are not configured, nothing to initialize, skipping...");
            return;
        }
        log.info("{} configured for Spring", Arrays.toString(scanPackages));

        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.scan(scanPackages);
        environment.servlets().addServletListeners(new ContextLoaderListener(applicationContext));
        applicationContext.refresh();
        applicationContext.start();
    }

}
