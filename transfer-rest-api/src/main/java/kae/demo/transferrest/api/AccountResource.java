package kae.demo.transferrest.api;

import kae.demo.transferrest.api.data.Account;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static kae.demo.transferrest.api.ResponseUtils.created;
import static kae.demo.transferrest.api.data.LocalEntityManagerFactory.executeAndReturn;
import static kae.demo.transferrest.api.data.LocalEntityManagerFactory.executeWithTransaction;

/**
 *
 */
@Path("users/{userId}/accounts")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class AccountResource {

  @POST
  public Response createAccount(@Context UriInfo uriInfo, @PathParam("userId") long userId) {
    Account account = new Account(0, userId);
    executeWithTransaction(
        (em) -> em.persist(account));

    return created(uriInfo, account.getId());
  }

  @GET
  public List<Account> getAccounts(@PathParam("userId") long userId) {
    return executeAndReturn(
        (em) -> {
          final CriteriaBuilder cb = em.getCriteriaBuilder();
          final CriteriaQuery<Account> cq = cb.createQuery(Account.class);
          final Root<Account> root = cq.from(Account.class);
          final ParameterExpression<Long> userIdParameter = cb.parameter(Long.class);

          cq.select(root)
              .where(cb.equal(root.get("userId"), userIdParameter))
              .orderBy(cb.asc(root.get("id")));

          final TypedQuery<Account> query = em.createQuery(cq);
          query.setParameter(userIdParameter, userId);

          return query.getResultList();
        });
  }

  @GET
  @Path("/{id}")
  public Account getAccount(@PathParam("userId") long userId, @PathParam("id") long id) {
    final Account account = executeAndReturn(
        (em) -> em.find(Account.class, id));
    if (account == null || account.getUserId() != userId) {
      throw new NotFoundException("User account has not been found by id " + id + " and userId " + userId);
    }
    return account;
  }

  @DELETE
  @Path("/{id}")
  public Response deleteAccount(@PathParam("userId") long userId, @PathParam("id") long id) {
    executeWithTransaction(
        (em) -> {
          final Account account = em.find(Account.class, id);
          if (account == null || account.getUserId() != userId) {
            throw new NotFoundException("User account has not been found by id " + id + " and userId " + userId);
          }
          em.remove(account);
        });
    return Response.noContent().build();
  }


}
