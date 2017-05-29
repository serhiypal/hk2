package org.serge.ws.rs.hk2.resource;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import java.util.Optional;

import org.serge.ws.rs.hk2.service.TracingService;
import org.serge.ws.rs.hk2.spring.Alive;
import org.serge.ws.rs.hk2.spring.HK2ServiceConsumer;

@Path("system")
@Consumes({ "application/json" })
@Produces({ "text/plain" })
public class SystemResource {

    private final TracingService tracingService;

    private final Alive alive;

    private final HK2ServiceConsumer serviceConsumer;

    @Inject
    public SystemResource(TracingService tracingService, Alive alive, HK2ServiceConsumer serviceConsumer) {
        this.tracingService = tracingService;
        this.alive = alive;
        this.serviceConsumer = serviceConsumer;
    }

    @Path("info")
    @GET
    public String info() {
        return tracingService.trace();
    }

    @Path("trace")
    @GET
    public String trace() {
        return serviceConsumer.value();
    }

    @Path("status")
    @GET
    public String status() {
        return alive.status();
    }

}
