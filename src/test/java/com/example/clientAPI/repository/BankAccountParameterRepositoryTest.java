package com.example.clientAPI.repository;

import dto.bankapi.BankAccountParameter;
import dto.bankapi.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
@Import(BankAccountParameterRepository.class)
class BankAccountParameterRepositoryTest {

    @Autowired
    private BankAccountParameterRepository bankAccountParameterRepository;

    private BankAccountParameter buildParameter(Double overdraftLimit, State state) {
        BankAccountParameter param = new BankAccountParameter();
        param.setOverdraftLimit(overdraftLimit);
        param.setState(state);
        return param;
    }

    // ==================== CREATE ====================

    @Test
    void testCreateParameter() {
        BankAccountParameter param = buildParameter(500.00, State.ACTIVE);

        BankAccountParameter created = bankAccountParameterRepository.createParameter(param);

        assertNotNull(created);
        assertNotNull(created.getId());
        assertTrue(created.getId() > 0);
        assertEquals(500.00, created.getOverdraftLimit());
        assertEquals(State.ACTIVE, created.getState());
    }

    @Test
    void testCreateParameterWithZeroOverdraft() {
        BankAccountParameter param = buildParameter(0.00, State.INACTIVE);

        BankAccountParameter created = bankAccountParameterRepository.createParameter(param);

        assertNotNull(created);
        assertEquals(0.00, created.getOverdraftLimit());
        assertEquals(State.INACTIVE, created.getState());
    }

    // ==================== READ ====================

    @Test
    void testGetAllParameters() {
        bankAccountParameterRepository.createParameter(buildParameter(100.00, State.ACTIVE));
        bankAccountParameterRepository.createParameter(buildParameter(200.00, State.INACTIVE));

        List<BankAccountParameter> result = bankAccountParameterRepository.getAllParameters();

        assertNotNull(result);
        assertTrue(result.size() >= 2);
    }

    // ==================== UPDATE ====================

    @Test
    void testUpdateState() {
        BankAccountParameter created = bankAccountParameterRepository.createParameter(
                buildParameter(300.00, State.ACTIVE));
        int id = created.getId();

        bankAccountParameterRepository.updateState(id, State.INACTIVE.toString());

        List<BankAccountParameter> all = bankAccountParameterRepository.getAllParameters();
        BankAccountParameter updated = all.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        assertNotNull(updated);
        assertEquals(State.INACTIVE, updated.getState());
    }

    @Test
    void testUpdateStateToBloqued() {
        BankAccountParameter created = bankAccountParameterRepository.createParameter(
                buildParameter(400.00, State.ACTIVE));
        int id = created.getId();

        bankAccountParameterRepository.updateState(id, State.BLOQUED.toString());

        List<BankAccountParameter> all = bankAccountParameterRepository.getAllParameters();
        BankAccountParameter updated = all.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        assertNotNull(updated);
        assertEquals(State.BLOQUED, updated.getState());
    }

    @Test
    void testUpdateOverdraftLimit() {
        BankAccountParameter created = bankAccountParameterRepository.createParameter(
                buildParameter(100.00, State.ACTIVE));
        int id = created.getId();

        bankAccountParameterRepository.updateOverdraftLimit(id, 2000.00);

        List<BankAccountParameter> all = bankAccountParameterRepository.getAllParameters();
        BankAccountParameter updated = all.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        assertNotNull(updated);
        assertEquals(2000.00, updated.getOverdraftLimit());
    }

    // ==================== DELETE ====================

    @Test
    void testDeleteParameter() {
        BankAccountParameter created = bankAccountParameterRepository.createParameter(
                buildParameter(500.00, State.ACTIVE));
        int id = created.getId();

        bankAccountParameterRepository.deleteParameter(id);

        List<BankAccountParameter> all = bankAccountParameterRepository.getAllParameters();
        boolean exists = all.stream().anyMatch(p -> p.getId() == id);

        assertFalse(exists);
    }
}
