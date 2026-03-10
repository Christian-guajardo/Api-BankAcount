package com.example.clientAPI.controller;

import com.example.clientAPI.business.BankAccountPivotBusiness;
import com.example.clientAPI.entity.BankAccountPivotEntity;
import com.example.clientAPI.mapper.BankAccountPivotMapper;
import dto.bankapi.BankAccountPivot;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@Path("bank/bank-accounts-pivot")
public class BankAccountPivotController {

    private final BankAccountPivotBusiness bankAccountPivotBusiness;

    public BankAccountPivotController(BankAccountPivotBusiness bankAccountPivotBusiness) {
        this.bankAccountPivotBusiness = bankAccountPivotBusiness;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createLink(BankAccountPivot requestDto) {
        BankAccountPivotEntity entity = BankAccountPivotMapper.toEntity(requestDto);
        bankAccountPivotBusiness.createLink(entity);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteLink(BankAccountPivot requestDto) {
        BankAccountPivotEntity entity = BankAccountPivotMapper.toEntity(requestDto);
        bankAccountPivotBusiness.deleteLink(entity);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/bank-account/{bankAccountId}")
    public Response deleteAllByBankAccount(@PathParam("bankAccountId") String bankAccountId) {
        bankAccountPivotBusiness.deleteAllByBankAccount(bankAccountId);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/account/{accountId}")
    public Response deleteAllByAccount(@PathParam("accountId") String accountId) {
        bankAccountPivotBusiness.deleteAllByAccount(accountId);
        return Response.noContent().build();
    }

    @GET
    @Path("/bank-account/{bankAccountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLinksByBankAccount(@PathParam("bankAccountId") String bankAccountId) {
        List<BankAccountPivotEntity> entities = bankAccountPivotBusiness.getLinksByBankAccount(bankAccountId);
        List<BankAccountPivot> dtos = entities.stream()
                .map(BankAccountPivotMapper::toDto)
                .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/account/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLinksByAccount(@PathParam("accountId") String accountId) {
        List<BankAccountPivotEntity> entities = bankAccountPivotBusiness.getLinksByAccount(accountId);
        List<BankAccountPivot> dtos = entities.stream()
                .map(BankAccountPivotMapper::toDto)
                .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }
}