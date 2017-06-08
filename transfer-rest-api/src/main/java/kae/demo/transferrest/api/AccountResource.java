package kae.demo.transferrest.api;

import kae.demo.transferrest.api.dto.Account;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 */
@Path("users/{userId}/accounts")
public class AccountResource {

  @POST
  public Response createAccount() {
    return Response.noContent().header("Location", "/accounts/" + (new Random().nextInt())).build();
  }

  @GET
  public List<Account> getAccounts(@PathParam("userId") long userId) {
    return Collections.singletonList(new Account(1, userId));
  }

  @GET
  @Path("/{id}")
  public Account getAccount(@PathParam("userId") long userId, @PathParam("id") long id) {
    return new Account(id, userId);
  }

  @DELETE
  @Path("/{id}")
  public Response deleteAccount(Account account) {
    return Response.noContent().build();
  }


}
