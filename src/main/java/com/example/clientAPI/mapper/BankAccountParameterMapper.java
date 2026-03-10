package com.example.clientAPI.mapper;

import com.example.clientAPI.entity.BankAccountParameterEntity;
import dto.bankapi.BankAccountParameter;

public class BankAccountParameterMapper {

    // DTO → Entity (BankAccountParameter)
    public static BankAccountParameterEntity toEntity(BankAccountParameter dto) {
        if (dto == null) return null;

        BankAccountParameterEntity entity = new BankAccountParameterEntity();
        entity.setId(dto.getId());
        entity.setOverdraftLimit(dto.getOverdraftLimit());
        entity.setState(dto.getState());

        return entity;
    }

    // Entity → DTO (BankAccountParameter)
    public static BankAccountParameter toDto(BankAccountParameterEntity entity) {
        if (entity == null) return null;

        BankAccountParameter dto = new BankAccountParameter();
        dto.setId(entity.getId());
        dto.setOverdraftLimit(entity.getOverdraftLimit());
        dto.setState(entity.getState());

        return dto;
    }
}

