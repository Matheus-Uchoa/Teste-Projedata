package com.example.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class);

    @Override
    public Response toResponse(Exception exception) {
        LOG.error("Exception caught in GlobalExceptionHandler", exception);
        
        // Handle resource not found
        if (exception instanceof ResourceNotFoundException) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(createErrorResponse(exception.getMessage()))
                    .build();
        }

        // Handle duplicate resource
        if (exception instanceof DuplicateResourceException) {
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity(createErrorResponse(exception.getMessage()))
                    .build();
        }

        // Handle referential integrity violation
        if (exception instanceof ReferentialIntegrityException) {
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity(createErrorResponse(exception.getMessage()))
                    .build();
        }

        // Handle JSON parsing errors
        if (exception instanceof WebApplicationException) {
            WebApplicationException wae = (WebApplicationException) exception;
            if (wae.getCause() instanceof JsonProcessingException) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(createErrorResponse("Invalid JSON format"))
                        .build();
            }
        }

        // Handle all other errors
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(createErrorResponse("Internal server error"))
                .build();
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return error;
    }
}
