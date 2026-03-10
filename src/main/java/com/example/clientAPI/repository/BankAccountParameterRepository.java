package com.example.clientAPI.repository;

import dto.bankapi.BankAccountParameter;
import dto.bankapi.State;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BankAccountParameterRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BankAccountParameterRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_GET_ALL_PARAMETERS =
            "SELECT id, overdraft_limit, state FROM BankAccountParameter";

    private static final String SQL_INSERT_PARAMETER =
            "INSERT INTO BankAccountParameter (overdraft_limit, state) " +
                    "VALUES (:overdraft_limit, :state)";

    private static final String SQL_UPDATE_STATE =
            "UPDATE BankAccountParameter SET state = :state WHERE id = :parameter_id";

    private static final String SQL_UPDATE_OVERDRAFT_LIMIT =
            "UPDATE BankAccountParameter SET overdraft_limit = :overdraft_limit WHERE id = :parameter_id";

    private static final String SQL_DELETE_PARAMETER =
            "DELETE FROM BankAccountParameter WHERE id = :parameter_id";

    public List<BankAccountParameter> getAllParameters() {
        return jdbcTemplate.query(SQL_GET_ALL_PARAMETERS, (rs, rowNum) -> {
            BankAccountParameter param = new BankAccountParameter();
            param.setId(rs.getInt("id"));
            param.setOverdraftLimit(rs.getDouble("overdraft_limit"));
            param.setState(State.fromValue(rs.getString("state")));
            return param;
        });
    }

    public BankAccountParameter createParameter(BankAccountParameter parameter) {
        Map<String, Object> params = new HashMap<>();
        params.put("overdraft_limit", parameter.getOverdraftLimit());
        params.put("state", parameter.getState().toString());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(SQL_INSERT_PARAMETER, new org.springframework.jdbc.core.namedparam.MapSqlParameterSource(params), keyHolder);

        if (keyHolder.getKey() != null) {
            parameter.setId(keyHolder.getKey().intValue());
        }

        return parameter;
    }

    public void updateState(Integer parameterId, String state) {
        Map<String, Object> params = new HashMap<>();
        params.put("parameter_id", parameterId);
        params.put("state", state);
        jdbcTemplate.update(SQL_UPDATE_STATE, params);
    }

    public void updateOverdraftLimit(Integer parameterId, Double overdraftLimit) {
        Map<String, Object> params = new HashMap<>();
        params.put("parameter_id", parameterId);
        params.put("overdraft_limit", overdraftLimit);
        jdbcTemplate.update(SQL_UPDATE_OVERDRAFT_LIMIT, params);
    }

    public void deleteParameter(Integer parameterId) {
        Map<String, Object> params = new HashMap<>();
        params.put("parameter_id", parameterId);
        jdbcTemplate.update(SQL_DELETE_PARAMETER, params);
    }
}