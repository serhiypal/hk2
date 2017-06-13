package org.serge.ws.rs.hk2.spring2;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("spring")
@Consumes({ "application/json" })
@Produces({ "text/plain" })
public class SpringResource {

    private final SpringConsumer springConsumer;

    @Inject
    public SpringResource(SpringConsumer springConsumer) {
        this.springConsumer = springConsumer;
    }

    @Path("status")
    @GET
    public String status() {
        return springConsumer.value();
    }

}
