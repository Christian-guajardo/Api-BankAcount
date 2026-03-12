package com.example.clientAPI.business;

import com.example.clientAPI.entity.TypesEntity;
import com.example.clientAPI.mapper.TypeMapper;
import com.example.clientAPI.repository.TypeRepository;
import dto.bankapi.Type;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeBusiness {

    private final TypeRepository typeRepository;

    public TypeBusiness(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public List<TypesEntity> getAllTypes() {
        List<Type> dtos = typeRepository.getAllTypes();
        return dtos.stream().map(TypeMapper::toEntity).collect(Collectors.toList());
    }

    public TypesEntity getTypeById(Integer id) {
        Type dto = typeRepository.getTypeById(id);
        if (dto == null) {
            throw new NotFoundException("Type non trouvé");
        }
        return TypeMapper.toEntity(dto);
    }

    public TypesEntity createType(TypesEntity entity) {
        if (entity.getName() == null || entity.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du type est obligatoire");
        }

        List<Type> existingTypes = typeRepository.getAllTypes();
        boolean exists = existingTypes.stream()
                .anyMatch(t -> t.getName().equalsIgnoreCase(entity.getName().trim()));
        if (exists) {
            throw new IllegalArgumentException("Ce type de compte existe déjà");
        }

        entity.setName(entity.getName().trim());

        Type dto = TypeMapper.toDto(entity);
        Type created = typeRepository.createType(dto);
        return TypeMapper.toEntity(created);
    }
}