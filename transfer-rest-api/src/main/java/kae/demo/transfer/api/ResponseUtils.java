package kae.demo.transfer.api;

import kae.demo.transfer.api.dto.MessageWrapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/** */
public class ResponseUtils {

  static Response internalServerError(String message) {
    return build(Response.Status.INTERNAL_SERVER_ERROR, message);
  }

  static Response created(UriInfo uriInfo, long id) {
    UriBuilder builder = uriInfo.getAbsolutePathBuilder();
    builder.path(Long.toString(id));
    return Response.created(builder.build()).build();
  }

  static Response build(Response.Status status, String message) {
    return buildWithMessage(Response.status(status), message);
  }

  static Response build(int status, String message) {
    return buildWithMessage(Response.status(status), message);
  }

  private static Response buildWithMessage(Response.ResponseBuilder builder, String message) {
    return builder
        .type(MediaType.APPLICATION_JSON_TYPE.withCharset("UTF-8"))
        .entity(new MessageWrapper(message))
        .build();
  }
}
