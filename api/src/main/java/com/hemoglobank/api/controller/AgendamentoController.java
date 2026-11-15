package com.hemoglobank.api.controller;

import com.hemoglobank.api.model.Agendamento;
import com.hemoglobank.api.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @GetMapping
    public List<Agendamento> listar() {
        return agendamentoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Agendamento> criar(@RequestBody Agendamento agendamento) {
        Agendamento salvo = agendamentoRepository.save(agendamento);
        return ResponseEntity.status(201).body(salvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        if (!agendamentoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        agendamentoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

@PostMapping("/medico/agendamento/atualizar/{id}")
public String atualizarAgendamento(
        @PathVariable Integer id, 
        @RequestParam("status") String status, 
        @RequestParam("observacoes") String observacoes) {
    
    Agendamento agendamento = agendamentoRepository.findById(id).orElse(null);
    if (agendamento != null) {
        agendamento.setAgeStatus(status);
        agendamento.setAgeObservacoes(observacoes);
        agendamentoRepository.save(agendamento);
    }
    return "redirect:/medico/painel";
}
}