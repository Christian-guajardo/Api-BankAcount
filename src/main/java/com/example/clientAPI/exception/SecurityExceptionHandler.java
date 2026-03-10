package com.example.clientAPI.exception;

import dto.bankapi.Error;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SecurityExceptionHandler implements ExceptionMapper<SecurityException> {

    @Override
    public Response toResponse(SecurityException exception) {
        Error error = new Error()
                .code("403")
                .message("Accès interdit")
                .details(exception.getMessage());

        return Response.status(Response.Status.FORBIDDEN)
                .entity(error)
                .build();
    }
}