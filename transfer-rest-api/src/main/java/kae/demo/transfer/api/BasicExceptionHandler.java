package kae.demo.transfer.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BasicExceptionHandler implements ExceptionMapper<Exception> {

  private static final Logger LOG = LoggerFactory.getLogger(BasicExceptionHandler.class);

  @Override
  public Response toResponse(Exception e) {
    LOG.warn("Unexpected exception", e);
    return ResponseUtils.internalServerError(e.getMessage());
  }
}
