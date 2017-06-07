package kae.demo.transferrest.api;

import kae.demo.transferrest.api.dto.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 */
@Path("users")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class UserResource {

  @POST
  public Response createUser(User user) {
    return Response.noContent().header("Location", "/users/" + (new Random().nextInt())).build();
  }

  @GET
  public List<User> getUsers() {
    return Collections.singletonList(new User(1, "Lev Tolstoy"));
  }

  @GET
  @Path("/{id}")
  public User getUser(@PathParam("id") long id) {
    return new User(id, "Fedor Dostoevsky");
  }

  @PUT
  @Path("/{id}")
  public Response updateUser(User user) {
    return Response.noContent().build();
  }

}
