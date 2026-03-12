package com.example.clientAPI.business;

import com.example.clientAPI.entity.BankAccountPivotEntity;
import com.example.clientAPI.mapper.BankAccountPivotMapper;
import com.example.clientAPI.repository.BankAccountPivotRepository;
import com.example.clientAPI.repository.BankAccountRepository;
import dto.bankapi.BankAccount;
import dto.bankapi.BankAccountPivot;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountPivotBusiness {

    private final BankAccountPivotRepository bankAccountPivotRepository;
    private final BankAccountRepository bankAccountRepository;

    public BankAccountPivotBusiness(BankAccountPivotRepository bankAccountPivotRepository,
                                    BankAccountRepository bankAccountRepository) {
        this.bankAccountPivotRepository = bankAccountPivotRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public void createLink(BankAccountPivotEntity entity) {
        BankAccount bankAccount = bankAccountRepository.getBankAccountById(entity.getBankAccountId());
        if (bankAccount == null) {
            throw new NotFoundException("Compte bancaire non trouvé");
        }
        BankAccountPivot dto = BankAccountPivotMapper.toDto(entity);
        bankAccountPivotRepository.createPivot(dto);
    }

    public void deleteLink(BankAccountPivotEntity entity) {
        BankAccount bankAccount = bankAccountRepository.getBankAccountById(entity.getBankAccountId());
        if (bankAccount == null) {
            throw new NotFoundException("Compte bancaire non trouvé");
        }
        BankAccountPivot dto = BankAccountPivotMapper.toDto(entity);
        bankAccountPivotRepository.deletePivot(dto);
    }

    public void deleteAllByBankAccount(String bankAccountId) {
        bankAccountPivotRepository.deleteAllPivotsByBankAccount(bankAccountId);
    }

    public void deleteAllByAccount(String accountId) {
        bankAccountPivotRepository.deleteAllPivotsByAccount(accountId);
    }

    public List<BankAccountPivotEntity> getLinksByBankAccount(String bankAccountId) {
        List<String> accountIds = bankAccountPivotRepository.getAccountsByBankAccount(bankAccountId);
        return BankAccountPivotMapper.accountIdsToEntities(bankAccountId, accountIds);
    }

    public List<BankAccountPivotEntity> getLinksByAccount(String accountId) {
        List<String> bankAccountIds = bankAccountPivotRepository.getBankAccountsByAccount(accountId);
        return BankAccountPivotMapper.bankAccountIdsToEntities(accountId, bankAccountIds);
    }

}