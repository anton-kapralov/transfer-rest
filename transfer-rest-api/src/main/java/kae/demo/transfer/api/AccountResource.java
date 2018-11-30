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
import kae.demo.transfer.account.Account;
import kae.demo.transfer.account.AccountService;

/** */
@Path("users/{userId}/accounts")
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public class AccountResource {

  @Inject private AccountService accountService;

  @POST
  public Response createAccount(@Context UriInfo uriInfo, @PathParam("userId") long userId) {
    long accountId = accountService.create(userId);
    return Response.created(uriInfo.getAbsolutePathBuilder().path(Long.toString(accountId)).build())
        .build();
  }

  @GET
  public List<Account> getAccounts(@PathParam("userId") long userId) {
    return accountService.list(userId);
  }

  @GET
  @Path("/{id}")
  public Account getAccount(@PathParam("userId") long userId, @PathParam("id") long id) {
    return accountService.get(userId, id);
  }

  @DELETE
  @Path("/{id}")
  public Response deleteAccount(@PathParam("userId") long userId, @PathParam("id") long id) {
    accountService.delete(userId, id);
    return Response.noContent().build();
  }
}
