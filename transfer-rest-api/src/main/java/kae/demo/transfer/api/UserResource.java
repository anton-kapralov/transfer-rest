package kae.demo.transfer.api;

import static javax.ws.rs.core.Response.created;
import static javax.ws.rs.core.Response.noContent;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import kae.demo.transfer.user.User;
import kae.demo.transfer.user.UserService;

/** */
@Path("users")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class UserResource {

  private final UserService userService;

  @Inject
  public UserResource(UserService userService) {
    this.userService = userService;
  }

  @POST
  public Response createUser(@Context UriInfo uriInfo, User user) {
    final long userId = userService.create(user);
    return created(uriInfo.getAbsolutePathBuilder().path(Long.toString(userId)).build()).build();
  }

  @GET
  public List<User> getUsers() {
    return userService.list();
  }

  @GET
  @Path("/{id}")
  public User getUser(@PathParam("id") long id) {
    return userService.get(id);
  }

  @PUT
  @Path("/{id}")
  public Response updateUser(@PathParam("id") long id, User userUpdate) {
    userService.update(id, userUpdate);
    return noContent().build();
  }

  @DELETE
  @Path("/{id}")
  public Response deleteUser(@PathParam("id") long id) {
    userService.delete(id);
    return noContent().build();
  }
}
