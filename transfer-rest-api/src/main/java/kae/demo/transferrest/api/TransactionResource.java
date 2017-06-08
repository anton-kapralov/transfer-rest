package kae.demo.transferrest.api;

import kae.demo.transferrest.api.dto.Transaction;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 */
@Path("users/{userId}/accounts/{accountId}/transactions")
public class TransactionResource {

  @POST
  public Response createTransaction(Transaction transaction) {
    return Response.noContent().header("Location", "/transactions/" + (new Random().nextInt())).build();
  }

  @GET
  public List<Transaction> getTransactions(@PathParam("userId") long userId, @PathParam("accountId") long accountId) {
    return Collections.singletonList(new Transaction(1, accountId, new Random().nextInt()));
  }

  @GET
  @Path("/{id}")
  public Transaction getTransaction(@PathParam("userId") long userId,
                                    @PathParam("accountId") long accountId,
                                    @PathParam("id") long id) {
    return new Transaction(id, accountId, new Random().nextInt());
  }

  @DELETE
  @Path("/{id}")
  public Response deleteTransaction(Transaction transaction) {
    return Response.noContent().build();
  }

}
