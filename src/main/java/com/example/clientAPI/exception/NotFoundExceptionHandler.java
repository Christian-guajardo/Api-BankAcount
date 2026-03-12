package com.example.clientAPI.exception;


import dto.bankapi.Error;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionHandler implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        Error error = new Error();
        error.setCode("404");
        error.setMessage(exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).entity(error).build();
    }
}
