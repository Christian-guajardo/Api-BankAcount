package com.example.clientAPI.mapper;

import com.example.clientAPI.entity.BankAccountPivotEntity;
import dto.bankapi.BankAccountPivot;

import java.util.List;
import java.util.stream.Collectors;

public class BankAccountPivotMapper {

    public static BankAccountPivotEntity toEntity(BankAccountPivot dto) {
        if (dto == null) return null;

        BankAccountPivotEntity entity = new BankAccountPivotEntity();
        entity.setBankAccountId(dto.getBankAccountId());
        // Conversion Integer → String pour compatibilité avec le DTO existant
        entity.setAccountId(dto.getAccountId());

        return entity;
    }

    public static BankAccountPivot toDto(BankAccountPivotEntity entity) {
        if (entity == null) return null;

        BankAccountPivot dto = new BankAccountPivot();
        dto.setBankAccountId(entity.getBankAccountId());
        // Conversion String → Integer pour compatibilité avec le DTO existant
        dto.setAccountId(entity.getAccountId());

        return dto;
    }

    /**
     * Convertit List<String> (accountIds) en List<Entity>
     * Utilisé quand on récupère les co-titulaires d'un compte
     */
    public static List<BankAccountPivotEntity> accountIdsToEntities(String bankAccountId, List<String> accountIds) {
        if (accountIds == null) return List.of();

        return accountIds.stream()
                .map(accountId -> {
                    BankAccountPivotEntity entity = new BankAccountPivotEntity();
                    entity.setBankAccountId(bankAccountId);
                    entity.setAccountId(accountId);
                    return entity;
                })
                .collect(Collectors.toList());
    }

    /**
     * Convertit List<String> (bankAccountIds) en List<Entity>
     * Utilisé quand on récupère les comptes d'un utilisateur
     */
    public static List<BankAccountPivotEntity> bankAccountIdsToEntities(String accountId, List<String> bankAccountIds) {
        if (bankAccountIds == null) return List.of();

        return bankAccountIds.stream()
                .map(bankAccountId -> {
                    BankAccountPivotEntity entity = new BankAccountPivotEntity();
                    entity.setBankAccountId(bankAccountId);
                    entity.setAccountId(accountId);
                    return entity;
                })
                .collect(Collectors.toList());
    }
}