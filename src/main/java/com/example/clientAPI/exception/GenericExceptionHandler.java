package com.example.clientAPI.exception;

import dto.bankapi.Error;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        Error error = new Error()
                .code("500")
                .message("Erreur interne du serveur")
                .details("Une erreur inattendue s'est produite");

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .build();
    }
}