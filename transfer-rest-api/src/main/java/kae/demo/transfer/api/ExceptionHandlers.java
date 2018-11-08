package kae.demo.transfer.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** */
@Slf4j
public class ExceptionHandlers {

  private static Response internalServerError(String message) {
    return build(Response.Status.INTERNAL_SERVER_ERROR, message);
  }

  private static Response build(Response.Status status, String message) {
    return buildWithMessage(Response.status(status), message);
  }

  private static Response build(int status, String message) {
    return buildWithMessage(Response.status(status), message);
  }

  private static Response buildWithMessage(Response.ResponseBuilder builder, String message) {
    return builder
        .type(MediaType.APPLICATION_JSON_TYPE.withCharset("UTF-8"))
        .entity(new Message(message))
        .build();
  }

  /** */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  private static class Message {
    private String message;
  }

  @Provider
  public static class BasicExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
      log.warn("Unexpected exception", e);
      return internalServerError(e.getMessage());
    }
  }

  @Provider
  public static class WebApplicationExceptionHandler
      implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException e) {
      return build(e.getResponse().getStatus(), e.getMessage());
    }
  }
}
