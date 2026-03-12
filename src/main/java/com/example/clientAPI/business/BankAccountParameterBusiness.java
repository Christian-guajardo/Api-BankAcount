package com.example.clientAPI.business;

import com.example.clientAPI.entity.BankAccountParameterEntity;
import com.example.clientAPI.mapper.BankAccountParameterMapper;
import com.example.clientAPI.repository.BankAccountParameterRepository;
import com.example.clientAPI.repository.BankAccountRepository;
import dto.bankapi.BankAccount;
import dto.bankapi.BankAccountParameter;
import dto.bankapi.State;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BankAccountParameterBusiness {

    private final BankAccountParameterRepository bankAccountParameterRepository;
    private final BankAccountRepository bankAccountRepository;

    public BankAccountParameterBusiness(
            BankAccountParameterRepository bankAccountParameterRepository,
            BankAccountRepository bankAccountRepository) {
        this.bankAccountParameterRepository = bankAccountParameterRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankAccountParameterEntity createParameterEntity(BankAccountParameterEntity parameterEntity) {
        BankAccountParameter dto = BankAccountParameterMapper.toDto(parameterEntity);

        if (dto.getOverdraftLimit() == null) {
            dto.setOverdraftLimit(0.0);
        }
        if (dto.getState() == null) {
            dto.setState(State.ACTIVE);
        }

        BankAccountParameter created = bankAccountParameterRepository.createParameter(dto);
        return BankAccountParameterMapper.toEntity(created);
    }

    public void updateParametersByBankAccountId(String bankAccountId, BankAccountParameterEntity parameterEntity) {
        BankAccount bankAccount = bankAccountRepository.getBankAccountById(bankAccountId);
        if (bankAccount == null) {
            throw new NotFoundException("Compte bancaire non trouvé");
        }

        if (parameterEntity.getOverdraftLimit() != null && parameterEntity.getOverdraftLimit() < 0) {
            throw new IllegalArgumentException("Le découvert autorisé ne peut pas être négatif");
        }

        BankAccountParameter dto = BankAccountParameterMapper.toDto(parameterEntity);

        if (dto.getOverdraftLimit() != null) {
            bankAccountParameterRepository.updateOverdraftLimit(
                    bankAccount.getParameterId(),
                    dto.getOverdraftLimit()
            );
        }

        if (dto.getState() != null) {
            bankAccountParameterRepository.updateState(
                    bankAccount.getParameterId(),
                    dto.getState().toString()
            );
        }
    }
}