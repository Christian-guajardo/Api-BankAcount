package com.example.clientAPI.business;

import com.example.clientAPI.entity.BankAccountPivotEntity;
import com.example.clientAPI.repository.BankAccountPivotRepository;
import com.example.clientAPI.repository.BankAccountRepository;
import dto.bankapi.BankAccount;
import dto.bankapi.BankAccountPivot;
import jakarta.ws.rs.NotFoundException;
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
class BankAccountPivotBusinessTest {

    @Mock
    private BankAccountPivotRepository bankAccountPivotRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountPivotBusiness bankAccountPivotBusiness;

    // ==================== Helpers ====================

    private BankAccountPivotEntity buildEntity(String bankAccountId, String accountId) {
        BankAccountPivotEntity entity = new BankAccountPivotEntity();
        entity.setBankAccountId(bankAccountId);
        entity.setAccountId(accountId);
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

    // ==================== createLink ====================

    @Test
    void testCreateLink() {
        when(bankAccountRepository.getBankAccountById("BA001")).thenReturn(validBankAccount());

        bankAccountPivotBusiness.createLink(buildEntity("BA001", "ACC-1"));

        verify(bankAccountPivotRepository).createPivot(any(BankAccountPivot.class));
    }

    @Test
    void testCreateLinkThrowsNotFoundExceptionWhenBankAccountNotFound() {
        when(bankAccountRepository.getBankAccountById("INEXISTANT")).thenReturn(null);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> bankAccountPivotBusiness.createLink(buildEntity("INEXISTANT", "ACC-1"))
        );

        assertTrue(ex.getMessage().contains("non trouvé"));
        verify(bankAccountPivotRepository, never()).createPivot(any());
    }

    // ==================== deleteLink ====================

    @Test
    void testDeleteLink() {
        when(bankAccountRepository.getBankAccountById("BA001")).thenReturn(validBankAccount());

        bankAccountPivotBusiness.deleteLink(buildEntity("BA001", "ACC-1"));

        verify(bankAccountPivotRepository).deletePivot(any(BankAccountPivot.class));
    }

    @Test
    void testDeleteLinkThrowsNotFoundExceptionWhenBankAccountNotFound() {
        when(bankAccountRepository.getBankAccountById("INEXISTANT")).thenReturn(null);

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> bankAccountPivotBusiness.deleteLink(buildEntity("INEXISTANT", "ACC-1"))
        );

        assertTrue(ex.getMessage().contains("non trouvé"));
        verify(bankAccountPivotRepository, never()).deletePivot(any());
    }

    // ==================== deleteAllByBankAccount ====================

    @Test
    void testDeleteAllByBankAccount() {
        bankAccountPivotBusiness.deleteAllByBankAccount("BA001");

        verify(bankAccountPivotRepository).deleteAllPivotsByBankAccount("BA001");
    }

    // ==================== deleteAllByAccount ====================

    @Test
    void testDeleteAllByAccount() {
        bankAccountPivotBusiness.deleteAllByAccount("ACC-1");

        verify(bankAccountPivotRepository).deleteAllPivotsByAccount("ACC-1");
    }

    // ==================== getLinksByBankAccount ====================

    @Test
    void testGetLinksByBankAccount() {
        when(bankAccountPivotRepository.getAccountsByBankAccount("BA001"))
                .thenReturn(List.of("ACC-1", "ACC-2"));

        List<BankAccountPivotEntity> result = bankAccountPivotBusiness.getLinksByBankAccount("BA001");

        assertEquals(2, result.size());
        assertEquals("BA001", result.get(0).getBankAccountId());
        assertEquals("ACC-1", result.get(0).getAccountId());
        assertEquals("BA001", result.get(1).getBankAccountId());
        assertEquals("ACC-2", result.get(1).getAccountId());
    }

    @Test
    void testGetLinksByBankAccountReturnsEmptyList() {
        when(bankAccountPivotRepository.getAccountsByBankAccount("BA001")).thenReturn(List.of());

        List<BankAccountPivotEntity> result = bankAccountPivotBusiness.getLinksByBankAccount("BA001");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ==================== getLinksByAccount ====================

    @Test
    void testGetLinksByAccount() {
        when(bankAccountPivotRepository.getBankAccountsByAccount("ACC-1"))
                .thenReturn(List.of("BA001", "BA002"));

        List<BankAccountPivotEntity> result = bankAccountPivotBusiness.getLinksByAccount("ACC-1");

        assertEquals(2, result.size());
        assertEquals("BA001", result.get(0).getBankAccountId());
        assertEquals("ACC-1", result.get(0).getAccountId());
        assertEquals("BA002", result.get(1).getBankAccountId());
        assertEquals("ACC-1", result.get(1).getAccountId());
    }

    @Test
    void testGetLinksByAccountReturnsEmptyList() {
        when(bankAccountPivotRepository.getBankAccountsByAccount("ACC-INEXISTANT")).thenReturn(List.of());

        List<BankAccountPivotEntity> result = bankAccountPivotBusiness.getLinksByAccount("ACC-INEXISTANT");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}