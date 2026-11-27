package com.hemoglobank.api.controller;

import com.hemoglobank.api.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/funcionarios")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    /** GET /api/funcionarios/por-email?email=fulano@hemocentro.com */
    @GetMapping("/por-email")
    public ResponseEntity<Map<String, Object>> porEmail(@RequestParam String email) {
        return service.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** GET /api/funcionarios/por-auth-id/{uuid}  (UUID inválido -> 400 automático) */
    @GetMapping("/por-auth-id/{authId}")
    public ResponseEntity<Map<String, Object>> porAuthId(@PathVariable UUID authId) {
        return service.buscarPorAuthId(authId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
