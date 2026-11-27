package com.hemoglobank.api.exception;

import com.hemoglobank.api.controller.EmployeeController;
import com.hemoglobank.api.controller.ReportingController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/** Vale só para os controllers novos; não mexe no comportamento do UsuarioController. */
@RestControllerAdvice(assignableTypes = {EmployeeController.class, ReportingController.class})
public class ApiExceptionHandler {

    @ExceptionHandler(ConsultaException.class)
    public ResponseEntity<Map<String, String>> banco(ConsultaException e) {
        // detalhe técnico só no log do servidor; o cliente recebe mensagem genérica
        System.err.println(e.getMessage() + " -> " + e.getCause());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("erro", "Não foi possível consultar o banco de dados."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> parametroInvalido(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
    }
}
