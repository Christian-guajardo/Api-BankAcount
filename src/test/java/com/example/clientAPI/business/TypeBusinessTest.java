package com.example.clientAPI.business;

import com.example.clientAPI.entity.TypesEntity;
import com.example.clientAPI.repository.TypeRepository;
import dto.bankapi.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TypeBusinessTest {

    @Mock
    private TypeRepository typeRepository;

    @InjectMocks
    private TypeBusiness typeBusiness;

    // ==================== Helpers ====================

    private Type buildTypeDto(Integer id, String name) {
        Type type = new Type();
        type.setId(id);
        type.setName(name);
        return type;
    }

    private TypesEntity buildTypeEntity(Integer id, String name) {
        TypesEntity entity = new TypesEntity();
        entity.setId(id);
        entity.setName(name);
        return entity;
    }

    // ==================== getAllTypes ====================

    @Test
    void testGetAllTypes() {
        when(typeRepository.getAllTypes()).thenReturn(List.of(
                buildTypeDto(1, "Compte Courant"),
                buildTypeDto(2, "Livret A")
        ));

        List<TypesEntity> result = typeBusiness.getAllTypes();

        assertEquals(2, result.size());
        assertEquals("Compte Courant", result.get(0).getName());
        assertEquals("Livret A", result.get(1).getName());
    }

    @Test
    void testGetAllTypesReturnsEmptyList() {
        when(typeRepository.getAllTypes()).thenReturn(List.of());

        List<TypesEntity> result = typeBusiness.getAllTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ==================== getTypeById ====================

    @Test
    void testGetTypeById() {
        when(typeRepository.getTypeById(1)).thenReturn(buildTypeDto(1, "Compte Courant"));

        TypesEntity result = typeBusiness.getTypeById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Compte Courant", result.getName());
    }

    @Test
    void testGetTypeByIdThrowsIllegalArgumentExceptionWhenNotFound() {
        when(typeRepository.getTypeById(9999)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> typeBusiness.getTypeById(9999)
        );

        assertTrue(ex.getMessage().contains("non trouvé"));
    }

    // ==================== createType ====================

    @Test
    void testCreateType() {
        when(typeRepository.getAllTypes()).thenReturn(List.of());
        when(typeRepository.createType(any())).thenReturn(buildTypeDto(5, "Livret Jeune"));

        TypesEntity entity = buildTypeEntity(null, "Livret Jeune");
        TypesEntity result = typeBusiness.createType(entity);

        assertNotNull(result);
        assertEquals(5, result.getId());
        assertEquals("Livret Jeune", result.getName());
    }

    @Test
    void testCreateTypeTrimsName() {
        when(typeRepository.getAllTypes()).thenReturn(List.of());
        when(typeRepository.createType(any())).thenReturn(buildTypeDto(6, "PEL"));

        TypesEntity entity = buildTypeEntity(null, "  PEL  ");
        typeBusiness.createType(entity);

        verify(typeRepository).createType(argThat(t -> "PEL".equals(t.getName())));
    }

    @Test
    void testCreateTypeThrowsIllegalArgumentExceptionOnNullName() {
        TypesEntity entity = buildTypeEntity(null, null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> typeBusiness.createType(entity)
        );

        assertTrue(ex.getMessage().contains("obligatoire"));
    }

    @Test
    void testCreateTypeThrowsIllegalArgumentExceptionOnBlankName() {
        TypesEntity entity = buildTypeEntity(null, "   ");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> typeBusiness.createType(entity)
        );

        assertTrue(ex.getMessage().contains("obligatoire"));
    }

    @Test
    void testCreateTypeThrowsIllegalArgumentExceptionOnDuplicateName() {
        when(typeRepository.getAllTypes()).thenReturn(List.of(
                buildTypeDto(1, "Compte Courant")
        ));

        TypesEntity entity = buildTypeEntity(null, "compte courant"); // case-insensitive

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> typeBusiness.createType(entity)
        );

        assertTrue(ex.getMessage().contains("déjà"));
    }

    @Test
    void testCreateTypeThrowsIllegalArgumentExceptionOnExactDuplicateName() {
        when(typeRepository.getAllTypes()).thenReturn(List.of(
                buildTypeDto(2, "Livret A")
        ));

        TypesEntity entity = buildTypeEntity(null, "Livret A");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> typeBusiness.createType(entity)
        );

        assertEquals("Ce type de compte existe déjà", ex.getMessage());
    }
}
