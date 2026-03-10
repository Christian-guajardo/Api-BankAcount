package com.example.clientAPI.mapper;

import com.example.clientAPI.entity.BankAccountDetailEntity;
import dto.bankapi.BankAccountDetail;


public class BankAccountDetailMapper {


    public static BankAccountDetail toDto(BankAccountDetailEntity entity) {
        if (entity == null) return null;

        BankAccountDetail dto = new BankAccountDetail();
        dto.setId(entity.getId());
        dto.setParameter(BankAccountParameterMapper.toDto(entity.getParameter()));
        dto.setType(TypeMapper.toDto(entity.getType()));
        dto.setSold(entity.getSold());
        dto.setIban(entity.getIban());

        return dto;
    }


    public static BankAccountDetailEntity toEntity(BankAccountDetail dto) {
        if (dto == null) return null;

        BankAccountDetailEntity entity = new BankAccountDetailEntity();
        entity.setId(dto.getId());
        entity.setParameter(BankAccountParameterMapper.toEntity(dto.getParameter()));
        entity.setType(TypeMapper.toEntity(dto.getType()));
        entity.setSold(dto.getSold());
        entity.setIban(dto.getIban());

        return entity;
    }
}