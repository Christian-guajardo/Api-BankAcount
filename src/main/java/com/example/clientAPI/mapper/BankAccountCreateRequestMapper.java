package com.example.clientAPI.mapper;

import com.example.clientAPI.entity.BankAccountEntity;
import com.example.clientAPI.entity.BankAccountParameterEntity;
import dto.bankapi.BankAccountCreateRequest;
import dto.bankapi.State;


public class BankAccountCreateRequestMapper {


    public static BankAccountParameterEntity toParameterEntity(BankAccountCreateRequest request) {
        if (request == null) return null;

        BankAccountParameterEntity parameter = new BankAccountParameterEntity();
        parameter.setOverdraftLimit(request.getOverdraftLimit());
        parameter.setState(request.getState() != null ? request.getState() : State.ACTIVE);

        return parameter;
    }


    public static BankAccountEntity toBankAccountEntity(BankAccountCreateRequest request) {
        if (request == null) return null;

        BankAccountEntity bankAccount = new BankAccountEntity();
        bankAccount.setTypeId(request.getTypeId());
        bankAccount.setSold(request.getSold());
        bankAccount.setIban(request.getIban());

        return bankAccount;
    }
}