package kae.demo.transfer.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/** */
@Path("/health")
public class HealthResource {

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response sayHello() {
    return Response.ok().entity("OK").build();
  }
}
