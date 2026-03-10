package com.example.clientAPI.repository;

import com.example.clientAPI.entity.*;
import dto.bankapi.BankAccount;
import dto.bankapi.BankAccountDetail;
import dto.bankapi.State;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BankAccountRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BankAccountRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_INSERT_BANK_ACCOUNT =
            "INSERT INTO BankAccount (id, parameter_id, type_id, sold, iban) " +
                    "VALUES (:id, :parameter_id, :type_id, :sold, :iban)";

    private static final String SQL_GET_BANK_ACCOUNT_BY_ID =
            "SELECT id, parameter_id, type_id, sold, iban " +
                    "FROM BankAccount WHERE id = :id";

    private static final String SQL_GET_ALL_BANK_ACCOUNTS =
            "SELECT id, parameter_id, type_id, sold, iban FROM BankAccount";

    private static final String SQL_GET_BANK_ACCOUNTS_BY_ACCOUNT_ID =
            "SELECT ba.id, ba.parameter_id, ba.type_id, ba.sold, ba.iban " +
                    "FROM BankAccount ba " +
                    "INNER JOIN BankAccountPivot pivot ON ba.id = pivot.bank_account_id " +
                    "WHERE pivot.account_id = :account_id";

    private static final String SQL_GET_ACTIVE_BANK_ACCOUNTS_BY_USER_ID =
            "SELECT ba.id, ba.parameter_id, ba.type_id, ba.sold, ba.iban " +
                    "FROM BankAccount ba " +
                    "INNER JOIN BankAccountPivot pivot ON ba.id = pivot.bank_account_id " +
                    "INNER JOIN BankAccountParameter bap ON ba.parameter_id = bap.id " +
                    "WHERE pivot.account_id = :account_id AND bap.state = 'active'";

    private static final String SQL_GET_ACTIVE_BANK_ACCOUNTS_BY_USER_ID_AND_TYPE =
            "SELECT ba.id, ba.parameter_id, ba.type_id, ba.sold, ba.iban " +
                    "FROM BankAccount ba " +
                    "INNER JOIN BankAccountPivot pivot ON ba.id = pivot.bank_account_id " +
                    "INNER JOIN BankAccountParameter bap ON ba.parameter_id = bap.id " +
                    "WHERE pivot.account_id = :account_id AND ba.type_id = :type_id AND bap.state = 'active'";

    private static final String SQL_GET_BANK_ACCOUNT_DETAIL_BY_ID =
            "SELECT ba.id, ba.parameter_id, ba.type_id, ba.sold, ba.iban, " +
                    "bap.id as param_id, bap.overdraft_limit, bap.state, " +
                    "t.id as type_id_val, t.name " +
                    "FROM BankAccount ba " +
                    "INNER JOIN BankAccountParameter bap ON ba.parameter_id = bap.id " +
                    "INNER JOIN Types t ON ba.type_id = t.id " +
                    "WHERE ba.id = :id";

    private static final String SQL_UPDATE_BANK_ACCOUNT =
            "UPDATE BankAccount SET " +
                    "parameter_id = :parameter_id, " +
                    "type_id = :type_id, " +
                    "sold = :sold, " +
                    "iban = :iban " +
                    "WHERE id = :id";

    private static final String SQL_DELETE_BANK_ACCOUNT =
            "DELETE FROM BankAccount WHERE id = :id";

    private static final String SQL_GET_BALANCE =
            "SELECT sold FROM BankAccount WHERE id = :id";

    public BankAccount createBankAccount(BankAccount bankAccount) {
        if (bankAccount.getId() == null || bankAccount.getId().isEmpty()) {
            bankAccount.setId(String.valueOf(System.currentTimeMillis()));
        }

        Map<String, Object> params = new HashMap<>();
        params.put("id", bankAccount.getId());
        params.put("parameter_id", bankAccount.getParameterId());
        params.put("type_id", bankAccount.getTypeId());
        params.put("sold", bankAccount.getSold());
        params.put("iban", bankAccount.getIban());

        jdbcTemplate.update(SQL_INSERT_BANK_ACCOUNT, params);
        return bankAccount;
    }

    public BankAccount getBankAccountById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        List<BankAccount> results = jdbcTemplate.query(SQL_GET_BANK_ACCOUNT_BY_ID, params, (rs, rowNum) -> {
            BankAccount ba = new BankAccount();
            ba.setId(rs.getString("id"));
            ba.setParameterId(rs.getInt("parameter_id"));
            ba.setTypeId(rs.getInt("type_id"));
            ba.setSold(rs.getDouble("sold"));
            ba.setIban(rs.getString("iban"));
            return ba;
        });

        return results.isEmpty() ? null : results.get(0);
    }

    public List<BankAccount> getAllBankAccounts() {
        return jdbcTemplate.query(SQL_GET_ALL_BANK_ACCOUNTS, this::mapBankAccount);
    }

    public List<BankAccount> getBankAccountsByAccountId(String accountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        return jdbcTemplate.query(SQL_GET_BANK_ACCOUNTS_BY_ACCOUNT_ID, params, this::mapBankAccount);
    }

    public List<BankAccount> getActiveBankAccountsByUserId(String accountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        return jdbcTemplate.query(SQL_GET_ACTIVE_BANK_ACCOUNTS_BY_USER_ID, params, this::mapBankAccount);
    }

    public List<BankAccount> getActiveBankAccountsByUserIdAndTypeId(String accountId, Integer typeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        params.put("type_id", typeId);
        return jdbcTemplate.query(SQL_GET_ACTIVE_BANK_ACCOUNTS_BY_USER_ID_AND_TYPE, params, this::mapBankAccount);
    }

    public BankAccountDetail getBankAccountDetailById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        List<BankAccountDetail> results = jdbcTemplate.query(
                SQL_GET_BANK_ACCOUNT_DETAIL_BY_ID, params, this::mapBankAccountDetail);

        return results.isEmpty() ? null : results.get(0);
    }

    public BankAccount updateBankAccount(String id, BankAccount bankAccount) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("parameter_id", bankAccount.getParameterId());
        params.put("type_id", bankAccount.getTypeId());
        params.put("sold", bankAccount.getSold());
        params.put("iban", bankAccount.getIban());

        jdbcTemplate.update(SQL_UPDATE_BANK_ACCOUNT, params);
        return bankAccount;
    }

    public void deleteBankAccount(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        jdbcTemplate.update(SQL_DELETE_BANK_ACCOUNT, params);
    }

    public Double getBalance(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<Double> results = jdbcTemplate.queryForList(SQL_GET_BALANCE, params, Double.class);
        return results.isEmpty() ? null : results.get(0);
    }

    private BankAccount mapBankAccount(java.sql.ResultSet rs, int rowNum)
            throws java.sql.SQLException {
        BankAccount ba = new BankAccount();
        ba.setId(rs.getString("id"));
        ba.setParameterId(rs.getInt("parameter_id"));
        ba.setTypeId(rs.getInt("type_id"));
        ba.setSold(rs.getDouble("sold"));
        ba.setIban(rs.getString("iban"));
        return ba;
    }

    private BankAccountDetail mapBankAccountDetail(java.sql.ResultSet rs, int rowNum)
            throws java.sql.SQLException {
        dto.bankapi.BankAccountParameter parameter = new dto.bankapi.BankAccountParameter();
        parameter.setId(rs.getInt("param_id"));
        parameter.setOverdraftLimit(rs.getDouble("overdraft_limit"));
        parameter.setState(State.fromValue(rs.getString("state")));

        dto.bankapi.Type type = new dto.bankapi.Type();
        type.setId(rs.getInt("type_id_val"));
        type.setName(rs.getString("name"));

        BankAccountDetail detail = new BankAccountDetail();
        detail.setId(rs.getString("id"));
        detail.setParameter(parameter);
        detail.setType(type);
        detail.setSold(rs.getDouble("sold"));
        detail.setIban(rs.getString("iban"));
        return detail;
    }
}