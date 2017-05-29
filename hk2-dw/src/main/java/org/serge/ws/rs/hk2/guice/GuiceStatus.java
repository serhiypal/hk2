package org.serge.ws.rs.hk2.guice;

import org.jvnet.hk2.guice.bridge.api.HK2Inject;
import org.serge.ws.rs.hk2.service.TracingService;

public class GuiceStatus implements Status {

    @HK2Inject
    private TracingService tracingService;

    @Override
    public String status() {
        return "Guice";
    }

    @Override
    public String trace() {
        return tracingService.trace();
    }
}
