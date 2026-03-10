package com.example.clientAPI.repository;

import dto.bankapi.BankAccount;
import dto.bankapi.BankAccountDetail;
import dto.bankapi.BankAccountParameter;
import dto.bankapi.State;
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
@Import(BankAccountRepository.class)
class BankAccountRepositoryTest {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    // Helpers pour créer des données de test
    // Note : BankAccount nécessite un parameter_id et type_id valides en base.
    // Le schema.sql de test doit initialiser BankAccountParameter et Types.
    // On part du principe que le schema.sql injecte au moins un paramètre (id=1) et un type (id=1).

    private BankAccount buildBankAccount(String id, Integer parameterId, Integer typeId, Double sold, String iban) {
        BankAccount ba = new BankAccount();
        ba.setId(id);
        ba.setParameterId(parameterId);
        ba.setTypeId(typeId);
        ba.setSold(sold);
        ba.setIban(iban);
        return ba;
    }

    // ==================== CREATE ====================

    @Test
    void testCreateBankAccount() {
        BankAccount ba = buildBankAccount("BA-TEST-1", 1, 1, 1000.00, "FR7600000000000000000000001");

        BankAccount created = bankAccountRepository.createBankAccount(ba);

        assertNotNull(created);
        assertEquals("BA-TEST-1", created.getId());
        assertEquals(1000.00, created.getSold());
        assertEquals("FR7600000000000000000000001", created.getIban());
    }

    @Test
    void testCreateBankAccountGeneratesIdWhenNull() {
        BankAccount ba = buildBankAccount(null, 1, 1, 500.00, "FR7600000000000000000000002");

        BankAccount created = bankAccountRepository.createBankAccount(ba);

        assertNotNull(created);
        assertNotNull(created.getId());
        assertFalse(created.getId().isBlank());
    }

    // ==================== READ ====================

    @Test
    void testGetBankAccountById() {
        BankAccount ba = buildBankAccount("BA-TEST-2", 1, 1, 200.00, "FR7600000000000000000000003");
        bankAccountRepository.createBankAccount(ba);

        BankAccount found = bankAccountRepository.getBankAccountById("BA-TEST-2");

        assertNotNull(found);
        assertEquals("BA-TEST-2", found.getId());
        assertEquals(200.00, found.getSold());
    }

    @Test
    void testGetBankAccountByIdReturnsNullWhenNotFound() {
        BankAccount result = bankAccountRepository.getBankAccountById("INEXISTANT");

        assertNull(result);
    }

    @Test
    void testGetAllBankAccounts() {
        bankAccountRepository.createBankAccount(
                buildBankAccount("BA-ALL-1", 1, 1, 100.00, "FR7600000000000000000000010"));
        bankAccountRepository.createBankAccount(
                buildBankAccount("BA-ALL-2", 1, 1, 200.00, "FR7600000000000000000000011"));

        List<BankAccount> result = bankAccountRepository.getAllBankAccounts();

        assertNotNull(result);
        assertTrue(result.size() >= 2);
    }

    @Test
    void testGetBankAccountDetailById() {
        BankAccount ba = buildBankAccount("BA-DETAIL-1", 1, 1, 750.00, "FR7600000000000000000000020");
        bankAccountRepository.createBankAccount(ba);

        BankAccountDetail detail = bankAccountRepository.getBankAccountDetailById("BA-DETAIL-1");

        assertNotNull(detail);
        assertEquals("BA-DETAIL-1", detail.getId());
        assertEquals(750.00, detail.getSold());
        assertNotNull(detail.getParameter());
        assertNotNull(detail.getType());
    }

    @Test
    void testGetBankAccountDetailByIdReturnsNullWhenNotFound() {
        BankAccountDetail result = bankAccountRepository.getBankAccountDetailById("INEXISTANT");

        assertNull(result);
    }

    // ==================== UPDATE ====================

    @Test
    void testUpdateBankAccount() {
        BankAccount ba = buildBankAccount("BA-UPDATE-1", 1, 1, 300.00, "FR7600000000000000000000030");
        bankAccountRepository.createBankAccount(ba);

        BankAccount updated = buildBankAccount("BA-UPDATE-1", 1, 1, 999.99, "FR7600000000000000000000030");
        bankAccountRepository.updateBankAccount("BA-UPDATE-1", updated);

        BankAccount found = bankAccountRepository.getBankAccountById("BA-UPDATE-1");
        assertEquals(999.99, found.getSold());
    }

    // ==================== DELETE ====================

    @Test
    void testDeleteBankAccount() {
        BankAccount ba = buildBankAccount("BA-DEL-1", 1, 1, 100.00, "FR7600000000000000000000040");
        bankAccountRepository.createBankAccount(ba);

        bankAccountRepository.deleteBankAccount("BA-DEL-1");

        BankAccount found = bankAccountRepository.getBankAccountById("BA-DEL-1");
        assertNull(found);
    }

    // ==================== BALANCE ====================

    @Test
    void testGetBalance() {
        BankAccount ba = buildBankAccount("BA-BAL-1", 1, 1, 1234.56, "FR7600000000000000000000050");
        bankAccountRepository.createBankAccount(ba);

        Double balance = bankAccountRepository.getBalance("BA-BAL-1");

        assertNotNull(balance);
        assertEquals(1234.56, balance);
    }

    @Test
    void testGetBalanceReturnsNullWhenNotFound() {
        Double balance = bankAccountRepository.getBalance("INEXISTANT");

        assertNull(balance);
    }
}
