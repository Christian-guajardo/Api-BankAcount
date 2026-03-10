package com.example.clientAPI.business;

import com.example.clientAPI.entity.BankAccountPivotEntity;
import com.example.clientAPI.repository.BankAccountPivotRepository;
import dto.bankapi.BankAccountPivot;
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

    @InjectMocks
    private BankAccountPivotBusiness bankAccountPivotBusiness;

    // ==================== Helpers ====================

    private BankAccountPivotEntity buildEntity(String bankAccountId, String accountId) {
        BankAccountPivotEntity entity = new BankAccountPivotEntity();
        entity.setBankAccountId(bankAccountId);
        entity.setAccountId(accountId);
        return entity;
    }

    // ==================== createLink ====================

    @Test
    void testCreateLink() {
        BankAccountPivotEntity entity = buildEntity("BA001", "ACC-1");

        bankAccountPivotBusiness.createLink(entity);

        verify(bankAccountPivotRepository).createPivot(any(BankAccountPivot.class));
    }

    // ==================== deleteLink ====================

    @Test
    void testDeleteLink() {
        BankAccountPivotEntity entity = buildEntity("BA001", "ACC-1");

        bankAccountPivotBusiness.deleteLink(entity);

        verify(bankAccountPivotRepository).deletePivot(any(BankAccountPivot.class));
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
        when(bankAccountPivotRepository.getAccountsByBankAccount("BA001"))
                .thenReturn(List.of());

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
        when(bankAccountPivotRepository.getBankAccountsByAccount("ACC-INEXISTANT"))
                .thenReturn(List.of());

        List<BankAccountPivotEntity> result = bankAccountPivotBusiness.getLinksByAccount("ACC-INEXISTANT");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
