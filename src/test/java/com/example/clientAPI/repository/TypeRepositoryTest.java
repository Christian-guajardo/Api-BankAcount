package com.example.clientAPI.repository;

import dto.bankapi.Type;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
@Import(TypeRepository.class)
class TypeRepositoryTest {

    @Autowired
    private TypeRepository typeRepository;

    private Type buildType(String name) {
        Type type = new Type();
        type.setName(name);
        return type;
    }

    // ==================== CREATE ====================

    @Test
    void testCreateType() {
        Type type = buildType("Livret Jeune");

        Type created = typeRepository.createType(type);

        assertNotNull(created);
        assertEquals("Livret Jeune", created.getName());
    }

    // ==================== READ ====================

    @Test
    void testGetAllTypes() {
        // Le schema.sql de test initialise déjà des types (ex: "Compte Courant", "Livret A" etc.)
        List<Type> result = typeRepository.getAllTypes();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetAllTypesAfterCreate() {
        int sizeBefore = typeRepository.getAllTypes().size();

        typeRepository.createType(buildType("Compte Pro"));

        List<Type> result = typeRepository.getAllTypes();
        assertEquals(sizeBefore + 1, result.size());
    }

    @Test
    void testGetTypeById() {
        // On récupère un type existant depuis l'initialisation du schema
        List<Type> all = typeRepository.getAllTypes();
        assertFalse(all.isEmpty());
        Integer existingId = all.get(0).getId();

        Type found = typeRepository.getTypeById(existingId);

        assertNotNull(found);
        assertEquals(existingId, found.getId());
        assertNotNull(found.getName());
    }

    @Test
    void testGetTypeByIdReturnsNullWhenNotFound() {
        Type result = typeRepository.getTypeById(9999);

        assertNull(result);
    }

    @Test
    void testGetTypeByIdMatchesName() {
        typeRepository.createType(buildType("PEL Test"));

        List<Type> all = typeRepository.getAllTypes();
        Type pelType = all.stream()
                .filter(t -> "PEL Test".equals(t.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(pelType);
        assertNotNull(pelType.getId());

        Type found = typeRepository.getTypeById(pelType.getId());
        assertEquals("PEL Test", found.getName());
    }
}
