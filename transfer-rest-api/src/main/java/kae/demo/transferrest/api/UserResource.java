package kae.demo.transferrest.api;

import kae.demo.transferrest.api.data.UserEntity;
import kae.demo.transferrest.api.dto.User;

import javax.persistence.EntityManager;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

import static kae.demo.transferrest.api.ResponseUtils.created;
import static kae.demo.transferrest.api.data.LocalEntityManagerFactory.executeAndReturn;
import static kae.demo.transferrest.api.data.LocalEntityManagerFactory.executeWithTransaction;

/** */
@Path("users")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class UserResource {

  @POST
  public Response createUser(@Context UriInfo uriInfo, User user) {
    UserEntity userEntity = new UserEntity(0, user.getName());
    executeWithTransaction((em) -> em.persist(userEntity));
    return created(uriInfo, userEntity.getId());
  }

  @GET
  public List<User> getUsers() {
    return executeAndReturn(
            (em) ->
                em.createQuery("SELECT u FROM UserEntity u ORDER BY u.name", UserEntity.class)
                    .getResultList())
        .stream()
        .map((this::toUserDTO))
        .collect(Collectors.toList());
  }

  @GET
  @Path("/{id}")
  public User getUser(@PathParam("id") long id) {
    return toUserDTO(getUserEntity(id));
  }

  @PUT
  @Path("/{id}")
  public Response updateUser(@PathParam("id") long id, UserEntity userUpdate) {
    final UserEntity userEntity = getUserEntity(id);

    final String name = userUpdate.getName();
    if (name != null && !name.isEmpty()) {
      userEntity.setName(name);
    }

    executeWithTransaction((em) -> em.merge(userEntity));

    return Response.noContent().build();
  }

  @DELETE
  @Path("/{id}")
  public Response deleteUser(@PathParam("id") long id) {
    executeWithTransaction((em) -> em.remove(getUserEntity(em, id)));
    return Response.noContent().build();
  }

  UserEntity getUserEntity(long id) {
    return executeAndReturn((em) -> getUserEntity(em, id));
  }

  private UserEntity getUserEntity(EntityManager em, long id) {
    final UserEntity userEntity = em.find(UserEntity.class, id);
    if (userEntity == null) {
      throw new NotFoundException("User has not been found by id " + id);
    }
    return userEntity;
  }

  private User toUserDTO(UserEntity userEntity) {
    return new User(userEntity.getId(), userEntity.getName());
  }
}
