package com.example.clientAPI.repository;

import dto.bankapi.BankAccountPivot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
@Import({BankAccountPivotRepository.class, BankAccountRepository.class, BankAccountParameterRepository.class})
class BankAccountPivotRepositoryTest {

    @Autowired
    private BankAccountPivotRepository bankAccountPivotRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankAccountParameterRepository bankAccountParameterRepository;

    // Crée un paramètre + un compte bancaire réels pour satisfaire les FK
    private String createBankAccount(String id, String iban) {
        dto.bankapi.BankAccountParameter param = new dto.bankapi.BankAccountParameter();
        param.setOverdraftLimit(0.00);
        param.setState(dto.bankapi.State.ACTIVE);
        dto.bankapi.BankAccountParameter created = bankAccountParameterRepository.createParameter(param);

        dto.bankapi.BankAccount ba = new dto.bankapi.BankAccount();
        ba.setId(id);
        ba.setParameterId(created.getId());
        ba.setTypeId(1);
        ba.setSold(100.00);
        ba.setIban(iban);
        bankAccountRepository.createBankAccount(ba);
        return id;
    }

    private BankAccountPivot buildPivot(String bankAccountId, String accountId) {
        BankAccountPivot pivot = new BankAccountPivot();
        pivot.setBankAccountId(bankAccountId);
        pivot.setAccountId(accountId);
        return pivot;
    }

    @BeforeEach
    void setUp() {
        createBankAccount("BA-PIVOT-1", "FR7600000000000000000000060");
        createBankAccount("BA-PIVOT-2", "FR7600000000000000000000061");
    }

    // ==================== CREATE ====================

    @Test
    void testCreatePivot() {
        BankAccountPivot pivot = buildPivot("BA-PIVOT-1", "ACC-001");

        bankAccountPivotRepository.createPivot(pivot);

        List<String> accounts = bankAccountPivotRepository.getAccountsByBankAccount("BA-PIVOT-1");
        assertTrue(accounts.contains("ACC-001"));
    }

    // ==================== READ ====================

    @Test
    void testGetAccountsByBankAccount() {
        bankAccountPivotRepository.createPivot(buildPivot("BA-PIVOT-1", "ACC-100"));
        bankAccountPivotRepository.createPivot(buildPivot("BA-PIVOT-1", "ACC-101"));

        List<String> accounts = bankAccountPivotRepository.getAccountsByBankAccount("BA-PIVOT-1");

        assertNotNull(accounts);
        assertTrue(accounts.contains("ACC-100"));
        assertTrue(accounts.contains("ACC-101"));
    }

    @Test
    void testGetAccountsByBankAccountReturnsEmptyWhenNone() {
        List<String> accounts = bankAccountPivotRepository.getAccountsByBankAccount("BA-PIVOT-2");

        assertNotNull(accounts);
        assertTrue(accounts.isEmpty());
    }

    @Test
    void testGetBankAccountsByAccount() {
        bankAccountPivotRepository.createPivot(buildPivot("BA-PIVOT-1", "ACC-200"));
        bankAccountPivotRepository.createPivot(buildPivot("BA-PIVOT-2", "ACC-200"));

        List<String> bankAccounts = bankAccountPivotRepository.getBankAccountsByAccount("ACC-200");

        assertNotNull(bankAccounts);
        assertEquals(2, bankAccounts.size());
        assertTrue(bankAccounts.contains("BA-PIVOT-1"));
        assertTrue(bankAccounts.contains("BA-PIVOT-2"));
    }

    @Test
    void testGetBankAccountsByAccountReturnsEmptyWhenNone() {
        List<String> bankAccounts = bankAccountPivotRepository.getBankAccountsByAccount("ACC-INEXISTANT");

        assertNotNull(bankAccounts);
        assertTrue(bankAccounts.isEmpty());
    }

    // ==================== DELETE ====================

    @Test
    void testDeletePivot() {
        bankAccountPivotRepository.createPivot(buildPivot("BA-PIVOT-1", "ACC-300"));

        bankAccountPivotRepository.deletePivot(buildPivot("BA-PIVOT-1", "ACC-300"));

        List<String> accounts = bankAccountPivotRepository.getAccountsByBankAccount("BA-PIVOT-1");
        assertFalse(accounts.contains("ACC-300"));
    }

    @Test
    void testDeleteAllPivotsByBankAccount() {
        bankAccountPivotRepository.createPivot(buildPivot("BA-PIVOT-1", "ACC-400"));
        bankAccountPivotRepository.createPivot(buildPivot("BA-PIVOT-1", "ACC-401"));

        bankAccountPivotRepository.deleteAllPivotsByBankAccount("BA-PIVOT-1");

        List<String> accounts = bankAccountPivotRepository.getAccountsByBankAccount("BA-PIVOT-1");
        assertTrue(accounts.isEmpty());
    }

    @Test
    void testDeleteAllPivotsByAccount() {
        bankAccountPivotRepository.createPivot(buildPivot("BA-PIVOT-1", "ACC-500"));
        bankAccountPivotRepository.createPivot(buildPivot("BA-PIVOT-2", "ACC-500"));

        bankAccountPivotRepository.deleteAllPivotsByAccount("ACC-500");

        List<String> bankAccounts = bankAccountPivotRepository.getBankAccountsByAccount("ACC-500");
        assertTrue(bankAccounts.isEmpty());
    }
}
