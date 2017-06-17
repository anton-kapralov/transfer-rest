package kae.demo.transferrest.api;

/**
 *
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionHandler implements ExceptionMapper<WebApplicationException> {

  @Override
  public Response toResponse(WebApplicationException e) {
    final Response response = e.getResponse();
    return Response.status(response.getStatus())
        .type(MediaType.APPLICATION_JSON_TYPE.withCharset("UTF-8"))
        .entity(new MessageWrapper(e.getMessage()))
        .build();
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  private static class MessageWrapper {

    private String message;

  }

}
