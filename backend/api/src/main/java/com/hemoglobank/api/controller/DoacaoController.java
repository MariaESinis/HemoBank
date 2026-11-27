package com.hemoglobank.api.controller;

import com.hemoglobank.api.model.Doacao;
import com.hemoglobank.api.repository.DoacaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doacoes")
public class DoacaoController {

    private final DoacaoRepository doacaoRepository;

    public DoacaoController(DoacaoRepository doacaoRepository) {
        this.doacaoRepository = doacaoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Doacao>> listarDoacoes() {
        List<Doacao> doacoes = doacaoRepository.findAll();
        if (doacoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(doacoes);
    }

    @PostMapping
    public ResponseEntity<Doacao> criarDoacao(@RequestBody Doacao novaDoacao) {
        novaDoacao.setDoaId(null); // evita sobrescrever um registro existente via id no JSON
        Doacao salva = doacaoRepository.save(novaDoacao);
        return ResponseEntity.status(201).body(salva);
    }
}
