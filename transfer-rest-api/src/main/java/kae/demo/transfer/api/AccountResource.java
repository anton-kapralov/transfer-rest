package kae.demo.transfer.api;

import static kae.demo.transfer.persistence.LocalEntityManagerFactory.executeAndReturn;
import static kae.demo.transfer.persistence.LocalEntityManagerFactory.executeAndReturnWithTransaction;
import static kae.demo.transfer.persistence.LocalEntityManagerFactory.executeWithTransaction;

import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import kae.demo.transfer.user.UserEntity;
import kae.demo.transfer.account.AccountEntity;
import kae.demo.transfer.account.Account;

/** */
@Path("users/{userId}/accounts")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class AccountResource {

  @Inject private UserResource userResource;

  @POST
  public Response createAccount(@Context UriInfo uriInfo, @PathParam("userId") long userId) {
    UserEntity userEntity = userResource.getUserEntity(userId);
    AccountEntity accountEntity = new AccountEntity(0, userEntity);
    executeWithTransaction((em) -> em.persist(accountEntity));

    return Response.created(
        uriInfo.getAbsolutePathBuilder().path(Long.toString(accountEntity.getId())).build())
        .build();
  }

  @GET
  public List<Account> getAccounts(@PathParam("userId") long userId) {
    return executeAndReturnWithTransaction(
        (EntityManager em) -> {
          final CriteriaBuilder cb = em.getCriteriaBuilder();
          final CriteriaQuery<AccountEntity> cq = cb.createQuery(AccountEntity.class);
          final Root<AccountEntity> root = cq.from(AccountEntity.class);
          final ParameterExpression<Long> userIdParameter = cb.parameter(Long.class);

          cq.select(root)
              .where(cb.equal(root.get("user").get("id"), userIdParameter))
              .orderBy(cb.asc(root.get("id")));

          final TypedQuery<AccountEntity> query = em.createQuery(cq);
          query.setParameter(userIdParameter, userId);

          return query
              .getResultList()
              .stream()
              .map(accountEntity -> toAccountDTO(em, accountEntity))
              .collect(Collectors.toList());
        });
  }

  @GET
  @Path("/{id}")
  public Account getAccount(@PathParam("userId") long userId, @PathParam("id") long id) {
    return executeAndReturnWithTransaction((em) -> toAccountDTO(em, getAccountEntity(em, id)));
  }

  @DELETE
  @Path("/{id}")
  public Response deleteAccount(@PathParam("userId") long userId, @PathParam("id") long id) {
    executeWithTransaction((em) -> em.remove(getAccountEntity(em, userId, id)));
    return Response.noContent().build();
  }

  AccountEntity getAccountEntity(long id) {
    return executeAndReturn((em) -> getAccountEntity(em, id));
  }

  private AccountEntity getAccountEntity(EntityManager em, long id) {
    final AccountEntity accountEntity = em.find(AccountEntity.class, id);
    if (accountEntity == null) {
      throw new NotFoundException("User account has not been found by id " + id);
    }
    return accountEntity;
  }

  AccountEntity getAccountEntity(long userId, long id) {
    return executeAndReturn((em) -> getAccountEntity(em, userId, id));
  }

  private AccountEntity getAccountEntity(EntityManager em, long userId, long id) {
    final AccountEntity accountEntity = em.find(AccountEntity.class, id);
    if (accountEntity == null || accountEntity.getUser().getId() != userId) {
      throw new NotFoundException(
          "User account has not been found by id " + id + " and userId " + userId);
    }
    return accountEntity;
  }

  private Account toAccountDTO(EntityManager em, AccountEntity accountEntity) {
    return new Account(
        accountEntity.getId(), accountEntity.getUser().getId(), accountEntity.getBalance());
  }
}