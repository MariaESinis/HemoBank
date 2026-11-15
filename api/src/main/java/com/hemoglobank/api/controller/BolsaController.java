package com.hemoglobank.api.controller;

import com.hemoglobank.api.model.Bolsa;
import com.hemoglobank.api.repository.BolsaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bolsas")
public class BolsaController {

    @Autowired
    private BolsaRepository bolsaRepository;

    @GetMapping
    public ResponseEntity<List<Bolsa>> listarBolsas() {
        List<Bolsa> bolsas = bolsaRepository.findAll();
        if (bolsas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bolsas);
    }

    @PostMapping
    public ResponseEntity<Bolsa> cadastrarBolsa(@RequestBody Bolsa novaBolsa) {
        Bolsa bolsaSalva = bolsaRepository.save(novaBolsa);
        return ResponseEntity.status(201).body(bolsaSalva);
    }
}