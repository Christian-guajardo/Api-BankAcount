package com.example.clientAPI.repository;

import dto.bankapi.Type;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TypeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TypeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_GET_ALL_TYPES =
            "SELECT id, name FROM Types";

    private static final String SQL_GET_TYPE_BY_ID =
            "SELECT id, name FROM Types WHERE id = :id";

    private static final String SQL_INSERT_TYPE =
            "INSERT INTO Types (name) VALUES (:name)";

    public List<Type> getAllTypes() {
        return jdbcTemplate.query(SQL_GET_ALL_TYPES, (rs, rowNum) -> {
            Type type = new Type();
            type.setId(rs.getInt("id"));
            type.setName(rs.getString("name"));
            return type;
        });
    }

    public Type getTypeById(Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        List<Type> results = jdbcTemplate.query(SQL_GET_TYPE_BY_ID, params, (rs, rowNum) -> {
            Type type = new Type();
            type.setId(rs.getInt("id"));
            type.setName(rs.getString("name"));
            return type;
        });

        return results.isEmpty() ? null : results.get(0);
    }

    public Type createType(Type type) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", type.getName());
        jdbcTemplate.update(SQL_INSERT_TYPE, params);
        return type;
    }
}