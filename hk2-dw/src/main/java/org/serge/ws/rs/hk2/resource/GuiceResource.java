package org.serge.ws.rs.hk2.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.serge.ws.rs.hk2.guice.Status;

@Path("guice")
@Consumes({ "application/json" })
@Produces({ "text/plain" })
public class GuiceResource {

    private final Status status;

    @Inject
    public GuiceResource(Status status) {
        this.status = status;
    }

    @Path("status")
    @GET
    public String status() {
        return status.status();
    }

    @Path("trace")
    @GET
    public String trace() {
        return status.trace();
    }

}
