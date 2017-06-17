package kae.demo.transferrest.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionHandler implements ExceptionMapper<WebApplicationException> {

  @Override
  public Response toResponse(WebApplicationException e) {
    return ResponseUtils.build(e.getResponse().getStatus(), e.getMessage());
  }

}
