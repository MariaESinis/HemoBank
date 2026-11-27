package com.hemoglobank.api.controller;

import com.hemoglobank.api.exception.RegraNegocioException;
import com.hemoglobank.api.model.Bolsa;
import com.hemoglobank.api.model.ComponenteSanguineo;
import com.hemoglobank.api.repository.BolsaRepository;
import com.hemoglobank.api.service.BolsaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/** API de bolsas para o perfil MÉDICO (ver SecurityConfig). */
@RestController
@RequestMapping("/api/bolsas")
public class BolsaController {

    private final BolsaRepository bolsaRepository;
    private final BolsaService bolsaService;

    public BolsaController(BolsaRepository bolsaRepository, BolsaService bolsaService) {
        this.bolsaRepository = bolsaRepository;
        this.bolsaService = bolsaService;
    }

    @GetMapping
    public ResponseEntity<List<Bolsa>> listarBolsas() {
        List<Bolsa> bolsas = bolsaRepository.findAll();
        if (bolsas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bolsas);
    }

    /** Mesmas regras do formulário do painel (valida CPF do doador, tipagem, validade...). */
    @PostMapping
    public ResponseEntity<?> cadastrarBolsa(@RequestBody NovaBolsaRequest req) {
        try {
            Bolsa salva = bolsaService.cadastrar(req.cpfDoador(), req.tipagem(), req.componente(),
                    req.quantidade(), req.validade());
            return ResponseEntity.status(201).body(salva);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    public record NovaBolsaRequest(String cpfDoador, String tipagem,
                                   ComponenteSanguineo componente,
                                   Integer quantidade, LocalDate validade) {
    }
}
