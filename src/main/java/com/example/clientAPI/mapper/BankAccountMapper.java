package com.example.clientAPI.mapper;

import com.example.clientAPI.entity.BankAccountEntity;
import dto.bankapi.BankAccount;


public class BankAccountMapper {


    public static BankAccount toDto(BankAccountEntity entity) {
        if (entity == null) return null;

        BankAccount dto = new BankAccount();
        dto.setId(entity.getId());
        dto.setParameterId(entity.getParameterId());
        dto.setTypeId(entity.getTypeId());
        dto.setSold(entity.getSold());
        dto.setIban(entity.getIban());

        return dto;
    }


    public static BankAccountEntity toEntity(BankAccount dto) {
        if (dto == null) return null;

        BankAccountEntity entity = new BankAccountEntity();
        entity.setId(dto.getId());
        entity.setParameterId(dto.getParameterId());
        entity.setTypeId(dto.getTypeId());
        entity.setSold(dto.getSold());
        entity.setIban(dto.getIban());

        return entity;
    }
}