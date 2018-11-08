package kae.demo.transfer.api;

import static kae.demo.transfer.api.ResponseUtils.created;
import static kae.demo.transfer.api.data.LocalEntityManagerFactory.executeAndReturn;
import static kae.demo.transfer.api.data.LocalEntityManagerFactory.executeWithTransaction;

import java.time.ZonedDateTime;
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
import kae.demo.transfer.api.dto.Transaction;
import kae.demo.transfer.api.data.AccountEntity;
import kae.demo.transfer.api.data.TransactionEntity;

/** */
@Path("users/{userId}/accounts/{accountId}/transactions")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class TransactionResource {

  @Inject private AccountResource accountResource;

  @POST
  public Response createTransaction(
      @Context UriInfo uriInfo,
      @PathParam("userId") long userId,
      @PathParam("accountId") long accountId,
      Transaction transaction) {
    TransactionEntity transactionEntity =
        new TransactionEntity(
            0,
            ZonedDateTime.now(),
            accountResource.getAccountEntity(userId, accountId),
            accountResource.getAccountEntity(transaction.getToAccountId()),
            transaction.getAmount(),
            transaction.getComment());

    executeWithTransaction(
        (em) -> {
          final AccountEntity fromAccount = transactionEntity.getFromAccount();
          fromAccount.updateBalance(transaction.getAmount().negate());
          em.merge(fromAccount);
          final AccountEntity toAccount = transactionEntity.getToAccount();
          toAccount.updateBalance(transaction.getAmount());
          em.merge(toAccount);
          em.persist(transactionEntity);
        });
    return created(uriInfo, transactionEntity.getId());
  }

  @GET
  public List<Transaction> getTransactions(
      @PathParam("userId") long userId, @PathParam("accountId") long accountId) {
    return executeAndReturn(
            (em) -> {
              final CriteriaBuilder cb = em.getCriteriaBuilder();
              final CriteriaQuery<TransactionEntity> cq = cb.createQuery(TransactionEntity.class);
              final Root<TransactionEntity> root = cq.from(TransactionEntity.class);
              final ParameterExpression<Long> userIdParameter = cb.parameter(Long.class);
              final ParameterExpression<Long> accountIdParameter = cb.parameter(Long.class);

              cq.select(root)
                  .where(
                      cb.or(
                          cb.and(
                              cb.equal(root.get("fromAccount").get("id"), accountIdParameter),
                              cb.equal(
                                  root.get("fromAccount").get("user").get("id"), userIdParameter)),
                          cb.and(
                              cb.equal(root.get("toAccount").get("id"), accountIdParameter),
                              cb.equal(
                                  root.get("toAccount").get("user").get("id"), userIdParameter))))
                  .orderBy(cb.asc(root.get("id")));

              final TypedQuery<TransactionEntity> query = em.createQuery(cq);
              query.setParameter(userIdParameter, userId);
              query.setParameter(accountIdParameter, accountId);

              return query.getResultList();
            })
        .stream()
        .map(this::toTransactionDTO)
        .collect(Collectors.toList());
  }

  @GET
  @Path("/{id}")
  public Transaction getTransaction(
      @PathParam("userId") long userId,
      @PathParam("accountId") long accountId,
      @PathParam("id") long id) {

    return toTransactionDTO(
        executeAndReturn((em) -> getTransactionEntity(em, userId, accountId, id)));
  }

  @DELETE
  @Path("/{id}")
  public Response deleteTransaction(
      @PathParam("userId") long userId,
      @PathParam("accountId") long accountId,
      @PathParam("id") long id) {
    executeWithTransaction((em -> em.remove(getTransactionEntity(em, userId, accountId, id))));
    return Response.noContent().build();
  }

  private TransactionEntity getTransactionEntity(
      EntityManager em, long userId, long accountId, long id) {
    final TransactionEntity transactionEntity = em.find(TransactionEntity.class, id);
    if (transactionEntity != null) {
      final boolean transactionOfFromAccount =
          transactionEntity.getFromAccount().getId() == accountId
              && transactionEntity.getFromAccount().getUser().getId() == userId;
      final boolean transactionOfToAccount =
          transactionEntity.getToAccount().getId() == accountId
              && transactionEntity.getToAccount().getUser().getId() == userId;

      if (transactionOfFromAccount || transactionOfToAccount) {
        return transactionEntity;
      }
    }
    throw new NotFoundException(
        "Transaction has not been found by id "
            + id
            + " and accountId "
            + accountId
            + " and userId "
            + userId);
  }

  private Transaction toTransactionDTO(TransactionEntity transactionEntity) {
    return new Transaction(
        transactionEntity.getId(),
        transactionEntity.getDateTime(),
        transactionEntity.getFromAccount().getId(),
        transactionEntity.getToAccount().getId(),
        transactionEntity.getAmount(),
        transactionEntity.getComment());
  }
}
