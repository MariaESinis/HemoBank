package com.hemoglobank.api.repository;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

/** Funções do schema hr, chamadas com JDBC puro. */
@Repository
public class EmployeeRepository {

    private static final String SQL_BY_EMAIL =
            "SELECT * FROM hr.fn_get_employee_by_email(?)";

    // O auth_id do Supabase é um UUID; o cast no SQL evita erro de tipo ao mandar String.
    private static final String SQL_BY_AUTH_ID =
            "SELECT * FROM hr.fn_get_employee_by_auth_id(?::uuid)";

    private final DataSource dataSource;

    public EmployeeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Optional<Map<String, Object>> findByEmail(String email) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_BY_EMAIL)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(JdbcRows.currentRow(rs)) : Optional.empty();
            }
        }
    }

    public Optional<Map<String, Object>> findByAuthId(String authId) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_BY_AUTH_ID)) {
            ps.setString(1, authId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(JdbcRows.currentRow(rs)) : Optional.empty();
            }
        }
    }
}
