package com.example.clientAPI.controller;

import com.example.clientAPI.business.BankAccountParameterBusiness;
import com.example.clientAPI.entity.BankAccountParameterEntity;
import com.example.clientAPI.mapper.BankAccountParameterMapper;
import dto.bankapi.BankAccountParameter;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Controller;

import java.util.Map;


@Controller
@Path("bank/admin/bank-accounts/{bank_account_id}/parameters")
public class BankAccountParameterController {

    private final BankAccountParameterBusiness bankAccountParameterBusiness;

    public BankAccountParameterController(BankAccountParameterBusiness bankAccountParameterBusiness) {
        this.bankAccountParameterBusiness = bankAccountParameterBusiness;
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateParameters(
            @PathParam("bank_account_id") String bankAccountId,
            BankAccountParameter requestDto) {

        BankAccountParameterEntity entity = BankAccountParameterMapper.toEntity(requestDto);
        bankAccountParameterBusiness.updateParametersByBankAccountId(bankAccountId, entity);

        return Response.ok(Map.of("message", "Paramètres mis à jour avec succès")).build();
    }
}