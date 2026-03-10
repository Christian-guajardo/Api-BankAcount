package com.example.clientAPI.controller;

import com.example.clientAPI.business.TypeBusiness;
import com.example.clientAPI.entity.TypesEntity;
import com.example.clientAPI.mapper.TypeMapper;
import dto.bankapi.Type;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@Path("bank/admin/types")
public class TypeController {

    private final TypeBusiness typeBusiness;

    public TypeController(TypeBusiness typeBusiness) {
        this.typeBusiness = typeBusiness;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTypes() {
        List<TypesEntity> entities = typeBusiness.getAllTypes();
        List<Type> dtos = entities.stream()
                .map(TypeMapper::toDto)
                .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTypeById(@PathParam("id") Integer id) {
        TypesEntity entity = typeBusiness.getTypeById(id);
        Type dto = TypeMapper.toDto(entity);
        return Response.ok(dto).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createType(Type requestDto) {
        TypesEntity entity = TypeMapper.toEntity(requestDto);
        TypesEntity createdEntity = typeBusiness.createType(entity);
        Type createdDto = TypeMapper.toDto(createdEntity);
        return Response.status(Response.Status.CREATED).entity(createdDto).build();
    }
}