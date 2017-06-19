package org.serge.ws.rs.jersey.provider.hk2;

import javax.inject.Inject;

import org.glassfish.hk2.api.ServiceLocator;
import org.jvnet.hk2.annotations.Service;

@Service
public class StatusServiceImpl implements StatusService {

    private final ServiceLocator serviceLocator;

    @Inject
    public StatusServiceImpl(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Override
    public String info() {
        return "Locator Id: " + serviceLocator.getLocatorId();
    }

}
