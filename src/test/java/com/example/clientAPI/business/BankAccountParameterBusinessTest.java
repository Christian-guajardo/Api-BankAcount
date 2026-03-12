package com.example.clientAPI.business;

import com.example.clientAPI.entity.BankAccountParameterEntity;
import com.example.clientAPI.repository.BankAccountParameterRepository;
import com.example.clientAPI.repository.BankAccountRepository;
import dto.bankapi.BankAccount;
import dto.bankapi.BankAccountParameter;
import dto.bankapi.State;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankAccountParameterBusinessTest {

    @Mock
    private BankAccountParameterRepository bankAccountParameterRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountParameterBusiness bankAccountParameterBusiness;

    // ==================== Helpers ====================

    private BankAccountParameterEntity validParameterEntity() {
        BankAccountParameterEntity entity = new BankAccountParameterEntity();
        entity.setOverdraftLimit(500.00);
        entity.setState(State.ACTIVE);
        return entity;
    }

    private BankAccount validBankAccount() {
        BankAccount ba = new BankAccount();
        ba.setId("BA001");
        ba.setParameterId(1);
        ba.setTypeId(1);
        ba.setSold(1000.00);
        ba.setIban("FR7612345678901234567890123");
        return ba;
    }

    // ==================== createParameterEntity ====================

    @Test
    void testCreateParameterEntity() {
        BankAccountParameter created = new BankAccountParameter();
        created.setId(1);
        created.setOverdraftLimit(500.00);
        created.setState(State.ACTIVE);
        when(bankAccountParameterRepository.createParameter(any())).thenReturn(created);

        BankAccountParameterEntity result = bankAccountParameterBusiness.createParameterEntity(validParameterEntity());

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(500.00, result.getOverdraftLimit());
        assertEquals(State.ACTIVE, result.getState());
    }

    @Test
    void testCreateParameterEntitySetsDefaultOverdraftLimitWhenNull() {
        BankAccountParameterEntity entity = new BankAccountParameterEntity();
        entity.setState(State.ACTIVE);
        entity.setOverdraftLimit(null);

        BankAccountParameter returned = new BankAccountParameter();
        returned.setId(2);
        returned.setOverdraftLimit(0.0);
        returned.setState(State.ACTIVE);
        when(bankAccountParameterRepository.createParameter(any())).thenReturn(returned);

        bankAccountParameterBusiness.createParameterEntity(entity);

        verify(bankAccountParameterRepository).createParameter(argThat(p -> p.getOverdraftLimit() == 0.0));
    }

    @Test
    void testCreateParameterEntitySetsDefaultStateWhenNull() {
        BankAccountParameterEntity entity = new BankAccountParameterEntity();
        entity.setOverdraftLimit(100.00);
        entity.setState(null);

        BankAccountParameter returned = new BankAccountParameter();
        returned.setId(3);
        returned.setOverdraftLimit(100.00);
        returned.setState(State.ACTIVE);
        when(bankAccountParameterRepository.createParameter(any())).thenReturn(returned);

        bankAccountParameterBusiness.createParameterEntity(entity);

        verify(bankAccountParameterRepository).createParameter(argThat(p -> State.ACTIVE.equals(p.getState())));
    }

    // ==================== updateParametersByBankAccountId ====================

    @Test
    void testUpdateParametersByBankAccountIdUpdatesOverdraftLimit() {
        when(bankAccountRepository.getBankAccountById("BA001")).thenReturn(validBankAccount());

        BankAccountParameterEntity entity = new BankAccountParameterEntity();
        entity.setOverdraftLimit(1000.00);

        bankAccountParameterBusiness.updateParametersByBankAccountId("BA001", entity);

        verify(bankAccountParameterRepository).updateOverdraftLimit(1, 1000.00);
        verify(bankAccountParameterRepository, never()).updateState(anyInt(), anyString());
    }

    @Test
    void testUpdateParametersByBankAccountIdUpdatesState() {
        when(bankAccountRepository.getBankAccountById("BA001")).thenReturn(validBankAccount());

        BankAccountParameterEntity entity = new BankAccountParameterEntity();
        entity.setState(State.INACTIVE);

        bankAccountParameterBusiness.updateParametersByBankAccountId("BA001", entity);

        verify(bankAccountParameterRepository).updateState(1, State.INACTIVE.toString());
        verify(bankAccountParameterRepository, never()).updateOverdraftLimit(anyInt(), anyDouble());
    }

    @Test
    void testUpdateParametersByBankAccountIdUpdatesBoth() {
        when(bankAccountRepository.getBankAccountById("BA001")).thenReturn(validBankAccount());

        BankAccountParameterEntity entity = new BankAccountParameterEntity();
        entity.setOverdraftLimit(2000.00);
        entity.setState(State.INACTIVE);

        bankAccountParameterBusiness.updateParametersByBankAccountId("BA001", entity);

        verify(bankAccountParameterRepository).updateOverdraftLimit(1, 2000.00);
        verify(bankAccountParameterRepository).updateState(1, State.INACTIVE.toString());
    }

    @Test
    void testUpdateParametersByBankAccountIdThrowsNotFoundExceptionWhenBankAccountNotFound() {
        when(bankAccountRepository.getBankAccountById("INEXISTANT")).thenReturn(null);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> bankAccountParameterBusiness.updateParametersByBankAccountId("INEXISTANT", validParameterEntity())
        );

        assertTrue(ex.getMessage().contains("non trouvé"));
    }

    @Test
    void testUpdateParametersByBankAccountIdThrowsIllegalArgumentExceptionOnNegativeOverdraft() {
        when(bankAccountRepository.getBankAccountById("BA001")).thenReturn(validBankAccount());

        BankAccountParameterEntity entity = new BankAccountParameterEntity();
        entity.setOverdraftLimit(-100.00);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> bankAccountParameterBusiness.updateParametersByBankAccountId("BA001", entity)
        );

        assertTrue(ex.getMessage().contains("négatif"));
    }
}