package com.example.clientAPI.controller;

import com.example.clientAPI.business.BankAccountBusiness;
import com.example.clientAPI.entity.BankAccountEntity;
import com.example.clientAPI.entity.BankAccountDetailEntity;
import com.example.clientAPI.mapper.BankAccountCreateRequestMapper;
import com.example.clientAPI.mapper.BankAccountDetailMapper;
import com.example.clientAPI.mapper.BankAccountMapper;
import dto.bankapi.BankAccount;
import dto.bankapi.BankAccountCreateRequest;
import dto.bankapi.BankAccountDetail;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@Path("/bank")
public class BankAccountController {

    private final BankAccountBusiness bankAccountBusiness;

    public BankAccountController(BankAccountBusiness bankAccountBusiness) {
        this.bankAccountBusiness = bankAccountBusiness;
    }

    // ==================== ROUTES ADMIN ====================

    @GET
    @Path("/admin/bank-accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBankAccounts() {
        List<BankAccountEntity> entities = bankAccountBusiness.getAllBankAccounts();
        List<BankAccount> dtos = entities.stream()
                .map(BankAccountMapper::toDto)
                .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/admin/bank-accounts/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAdminBankAccountById(@PathParam("id") String id) {
        BankAccountDetailEntity entity = bankAccountBusiness.getBankAccountDetailById(id);
        BankAccountDetail dto = BankAccountDetailMapper.toDto(entity);
        return Response.ok(dto).build();
    }

    @DELETE
    @Path("/admin/bank-accounts/{id}")
    public Response deleteBankAccount(@PathParam("id") String id) {
        bankAccountBusiness.deleteBankAccount(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/admin/accounts/{account_id}/bank-accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAdminBankAccountsByAccountId(@PathParam("account_id") String accountId) {
        List<BankAccountEntity> entities = bankAccountBusiness.getBankAccountsByAccountId(accountId);
        List<BankAccount> dtos = entities.stream()
                .map(BankAccountMapper::toDto)
                .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @POST
    @Path("/admin/accounts/{account_id}/bank-accounts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBankAccount(
            @PathParam("account_id") String accountId,
            BankAccountCreateRequest requestDto) {

        BankAccountDetailEntity createdEntity = bankAccountBusiness.createBankAccountForUser(
                accountId,
                BankAccountCreateRequestMapper.toBankAccountEntity(requestDto),
                BankAccountCreateRequestMapper.toParameterEntity(requestDto)
        );

        BankAccountDetail createdDto = BankAccountDetailMapper.toDto(createdEntity);
        return Response.status(Response.Status.CREATED).entity(createdDto).build();
    }

    // ==================== ROUTES CLIENT ====================

    @GET
    @Path("/my-bank-accounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyBankAccounts(
            @QueryParam("type_id") Integer typeId,
            @HeaderParam("X-User-Id") String userId) {

        List<BankAccountEntity> entities = bankAccountBusiness.getMyBankAccounts(userId, typeId);
        List<BankAccount> dtos = entities.stream()
                .map(BankAccountMapper::toDto)
                .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/my-bank-accounts/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyBankAccountById(
            @PathParam("id") String id,
            @HeaderParam("X-User-Id") String userId) {

        BankAccountDetailEntity entity = bankAccountBusiness.getMyBankAccountById(userId, id);
        BankAccountDetail dto = BankAccountDetailMapper.toDto(entity);
        return Response.ok(dto).build();
    }

    @GET
    @Path("/my-bank-accounts/{id}/co-holders")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyCoHolders(
            @PathParam("id") String id,
            @HeaderParam("X-User-Id") String userId) {

        List<String> coHolderIds = bankAccountBusiness.getCoHolderIds(userId, id);
        return Response.ok(coHolderIds).build();
    }
}