package kae.demo.transfer.api;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import kae.demo.transfer.transaction.Transaction;
import kae.demo.transfer.transaction.TransactionService;

/** */
@Path("users/{userId}/accounts/{accountId}/transactions")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class TransactionResource {

  @Inject private TransactionService transactionService;

  @POST
  public Response createTransaction(
      @Context UriInfo uriInfo,
      @PathParam("userId") long userId,
      @PathParam("accountId") long accountId,
      Transaction transaction) {
    long transactionId = transactionService.create(userId, accountId, transaction);
    return Response.created(
            uriInfo.getAbsolutePathBuilder().path(Long.toString(transactionId)).build())
        .build();
  }

  @GET
  public List<Transaction> getTransactions(
      @PathParam("userId") long userId, @PathParam("accountId") long accountId) {
    return transactionService.list(userId, accountId);
  }

  @GET
  @Path("/{id}")
  public Transaction getTransaction(
      @PathParam("userId") long userId,
      @PathParam("accountId") long accountId,
      @PathParam("id") long id) {
    return transactionService.get(userId, accountId, id);
  }

  @DELETE
  @Path("/{id}")
  public Response deleteTransaction(
      @PathParam("userId") long userId,
      @PathParam("accountId") long accountId,
      @PathParam("id") long id) {
    transactionService.delete(userId, accountId, id);
    return Response.noContent().build();
  }
}
