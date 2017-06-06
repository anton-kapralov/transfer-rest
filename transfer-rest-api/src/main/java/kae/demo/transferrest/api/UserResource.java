package kae.demo.transferrest.api;

import kae.demo.transferrest.api.dto.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

/**
 *
 */
@Path("users")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class UserResource {

  @GET
  public List<User> getUsers() {
    return Collections.singletonList(new User(1, "Lev Tolstoy"));
  }

  @GET
  @Path("/{id}")
  public User getUser(@PathParam("id") long id) {
    return new User(id, "Fedor Dostoevsky");
  }

}
