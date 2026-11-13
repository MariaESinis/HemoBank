package com.hemoglobank.api.controller;

import com.hemoglobank.api.model.Estoque;
import com.hemoglobank.api.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @GetMapping
    public ResponseEntity<List<Estoque>> listarEstoque() {
        List<Estoque> estoques = estoqueRepository.findAll();
        if (estoques.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estoques);
    }

    @PostMapping
    public ResponseEntity<Estoque> criarEstoque(@RequestBody Estoque novoEstoque) {
        Estoque salvo = estoqueRepository.save(novoEstoque);
        return ResponseEntity.status(201).body(salvo);
    }
}