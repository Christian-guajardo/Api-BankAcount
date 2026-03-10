package com.example.clientAPI.mapper;

import com.example.clientAPI.entity.TypesEntity;
import dto.bankapi.Type;

public class TypeMapper {

    // DTO → Entity (Type)
    public static TypesEntity toEntity(Type dto) {
        if (dto == null) return null;

        TypesEntity entity = new TypesEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());

        return entity;
    }

    // Entity → DTO (Type)
    public static Type toDto(TypesEntity entity) {
        if (entity == null) return null;

        Type dto = new Type();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }
}

