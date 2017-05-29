package org.serge.ws.rs.hk2.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.serge.ws.rs.hk2.service.UserService;
import org.serge.ws.rs.hk2.dto.User;
import org.serge.ws.rs.hk2.dto.ResourceCollection;

@Path("users")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class UserResource {

    private final UserService userService;

    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    public ResourceCollection<User> list() {
        return new ResourceCollection<>(userService.list());
    }

}
