package kae.demo.transferrest.api;

import kae.demo.transferrest.api.data.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static kae.demo.transferrest.api.data.LocalEntityManagerFactory.executeAndReturn;
import static kae.demo.transferrest.api.data.LocalEntityManagerFactory.executeWithTransaction;

/**
 *
 */
@Path("users")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class UserResource {

  @POST
  public Response createUser(@Context UriInfo uriInfo, User user) {
    executeWithTransaction(
        (em) -> em.persist(user));
    return Response
        .noContent()
        .header("Location", uriInfo.getBaseUri() + "users/" + user.getId()).build();
  }

  @GET
  public List<User> getUsers() {
    return executeAndReturn(
        (em) -> em.createQuery("SELECT u FROM User u ORDER BY u.name", User.class).getResultList());
  }

  @GET
  @Path("/{id}")
  public User getUser(@PathParam("id") long id) {
    final User user = executeAndReturn(
        (em) -> em.find(User.class, id));
    if (user == null) {
      throw new NotFoundException("User has not been found by id " + id);
    }
    return user;
  }

  @PUT
  @Path("/{id}")
  public Response updateUser(@PathParam("id") long id, User userUpdate) {
    final User user = getUser(id);

    final String name = userUpdate.getName();
    if (name != null && !name.isEmpty()) {
      user.setName(name);
    }

    executeWithTransaction(
        (em) -> em.merge(user));

    return Response.noContent().build();
  }

  @DELETE
  @Path("/{id}")
  public Response deleteUser(@PathParam("id") long id) {
    executeWithTransaction(
        (em) -> {
          final User user = em.find(User.class, id);
          if (user == null) {
            throw new NotFoundException("User has not been found by id " + id);
          }
          em.remove(user);
        });
    return Response.noContent().build();
  }

}
