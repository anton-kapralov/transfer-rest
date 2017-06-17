package kae.demo.transferrest.api;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
public class ResponseUtils {

  static Response internalServerError(String message) {
    return build(Response.Status.INTERNAL_SERVER_ERROR, message);
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
