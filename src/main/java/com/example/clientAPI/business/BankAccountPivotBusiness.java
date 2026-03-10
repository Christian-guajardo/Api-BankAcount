package com.example.clientAPI.business;

import com.example.clientAPI.entity.BankAccountPivotEntity;
import com.example.clientAPI.mapper.BankAccountPivotMapper;
import com.example.clientAPI.repository.BankAccountPivotRepository;
import dto.bankapi.BankAccountPivot;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountPivotBusiness {

    private final BankAccountPivotRepository bankAccountPivotRepository;

    public BankAccountPivotBusiness(BankAccountPivotRepository bankAccountPivotRepository) {
        this.bankAccountPivotRepository = bankAccountPivotRepository;
    }

    public void createLink(BankAccountPivotEntity entity) {
        BankAccountPivot dto = BankAccountPivotMapper.toDto(entity);
        bankAccountPivotRepository.createPivot(dto);
    }

    public void deleteLink(BankAccountPivotEntity entity) {
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

    public List<String> getCoHolderIds(String userId, String bankAccountId) {
        List<String> accountIds = bankAccountPivotRepository.getAccountsByBankAccount(bankAccountId);
        if (!accountIds.contains(userId)) {
            throw new SecurityException("Ce compte ne vous appartient pas");
        }
        return accountIds.stream()
                .filter(id -> !id.equals(userId))
                .toList();
    }
}