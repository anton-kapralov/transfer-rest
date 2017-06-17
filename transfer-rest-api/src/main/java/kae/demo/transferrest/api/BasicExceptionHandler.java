package kae.demo.transferrest.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static kae.demo.transferrest.api.ResponseUtils.internalServerError;

@Provider
public class BasicExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        return internalServerError(e.getMessage());
    }

}
