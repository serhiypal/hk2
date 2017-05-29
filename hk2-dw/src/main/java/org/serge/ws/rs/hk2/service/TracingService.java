package org.serge.ws.rs.hk2.service;

import javax.inject.Inject;

import org.glassfish.hk2.api.ServiceLocator;
import org.jvnet.hk2.annotations.Contract;
import org.jvnet.hk2.annotations.Service;
import org.serge.ws.rs.hk2.jersey.Spring;

@Contract
@Service
@Spring
public class TracingService {

    private final ServiceLocator serviceLocator;

    @Inject
    public TracingService(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    public String trace() {
        return String.format("Name: %s, Locator id: %s", serviceLocator.getName(),serviceLocator.getLocatorId());
    }

}
