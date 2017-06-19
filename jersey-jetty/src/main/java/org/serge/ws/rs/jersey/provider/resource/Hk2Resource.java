package org.serge.ws.rs.jersey.provider.resource;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.serge.ws.rs.jersey.provider.hk2.StatusService;

@Resource
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/hk2")
public class Hk2Resource {

    private final StatusService statusService;

    @Inject
    public Hk2Resource(StatusService statusService) {
        this.statusService = statusService;
    }

    @GET
    @Path("status")
    public String status() {
        return statusService.info();
    }

}
