package com.example.clientAPI.exception;

import dto.bankapi.Error;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionHandler implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        Error errorResponse = new Error();
        errorResponse.setCode("400");
        errorResponse.setMessage("Requête invalide");
        errorResponse.setDetails(exception.getMessage());

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build();
    }
}