package com.hemoglobank.api.service;

import com.hemoglobank.api.exception.ConsultaException;
import com.hemoglobank.api.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Optional<Map<String, Object>> buscarPorEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Informe o e-mail.");
        }
        try {
            return repository.findByEmail(email.trim());
        } catch (SQLException e) {
            throw new ConsultaException("Falha ao executar hr.fn_get_employee_by_email", e);
        }
    }

    public Optional<Map<String, Object>> buscarPorAuthId(UUID authId) {
        try {
            return repository.findByAuthId(authId.toString());
        } catch (SQLException e) {
            throw new ConsultaException("Falha ao executar hr.fn_get_employee_by_auth_id", e);
        }
    }
}
