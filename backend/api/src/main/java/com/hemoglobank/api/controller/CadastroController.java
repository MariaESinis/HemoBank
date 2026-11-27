package com.hemoglobank.api.controller;

import com.hemoglobank.api.dto.DoadorRegistroDTO;
import com.hemoglobank.api.exception.RegraNegocioException;
import com.hemoglobank.api.model.Doador;
import com.hemoglobank.api.service.CadastroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cadastro")
public class CadastroController {

    private final CadastroService cadastroService;

    public CadastroController(CadastroService cadastroService) {
        this.cadastroService = cadastroService;
    }

    @PostMapping("/doador")
    public ResponseEntity<Map<String, Object>> cadastrarDoador(@Valid @RequestBody DoadorRegistroDTO dto,
                                                               BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> campos = new LinkedHashMap<>();
            resultado.getFieldErrors().forEach(e -> campos.putIfAbsent(e.getField(), e.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of("erro", "Dados inválidos.", "campos", campos));
        }

        try {
            Doador doador = cadastroService.cadastrarDoador(dto);
            // Devolve só o necessário (nunca a entidade, que referencia a conta/senha)
            return ResponseEntity.status(201).body(Map.of("id", doador.getDorId(), "nome", doador.getDorNome()));
        } catch (RegraNegocioException e) {
            return ResponseEntity.status(409).body(Map.of("erro", e.getMessage()));
        }
    }
}
