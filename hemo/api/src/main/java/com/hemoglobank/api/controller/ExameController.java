package com.hemoglobank.api.controller;

import com.hemoglobank.api.model.Exame;
import com.hemoglobank.api.repository.ExameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exames")
public class ExameController {

    @Autowired
    private ExameRepository exameRepository;

    @GetMapping
    public List<Exame> listar() {
        return exameRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Exame> criar(@RequestBody Exame exame) {
        Exame salvo = exameRepository.save(exame);
        return ResponseEntity.status(201).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exame> atualizar(@PathVariable Integer id, @RequestBody Exame exameAtualizado) {
        return exameRepository.findById(id).map(exame -> {
            exame.setExaTipo(exameAtualizado.getExaTipo());
            exame.setExaResultado(exameAtualizado.getExaResultado());
            exame.setExaObservacoes(exameAtualizado.getExaObservacoes());
            Exame salvo = exameRepository.save(exame);
            return ResponseEntity.ok(salvo);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        if (!exameRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        exameRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}