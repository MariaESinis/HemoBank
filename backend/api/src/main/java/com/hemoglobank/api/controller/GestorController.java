package com.hemoglobank.api.controller;

import com.hemoglobank.api.model.StatusAgendamento;
import com.hemoglobank.api.model.StatusBolsa;
import com.hemoglobank.api.model.StatusExame;
import com.hemoglobank.api.repository.AgendamentoRepository;
import com.hemoglobank.api.repository.BolsaRepository;
import com.hemoglobank.api.repository.DoadorRepository;
import com.hemoglobank.api.repository.ExameRepository;
import com.hemoglobank.api.service.EstoqueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

/** Painel do gestor (/gestor/**): visão geral do hemocentro. */
@Controller
public class GestorController {

    /** Janela (em dias) para alertar bolsas próximas do vencimento. */
    private static final int DIAS_ALERTA_VALIDADE = 7;

    private final DoadorRepository doadorRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final ExameRepository exameRepository;
    private final BolsaRepository bolsaRepository;
    private final EstoqueService estoqueService;

    public GestorController(DoadorRepository doadorRepository,
                            AgendamentoRepository agendamentoRepository,
                            ExameRepository exameRepository,
                            BolsaRepository bolsaRepository,
                            EstoqueService estoqueService) {
        this.doadorRepository = doadorRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.exameRepository = exameRepository;
        this.bolsaRepository = bolsaRepository;
        this.estoqueService = estoqueService;
    }

    @GetMapping("/gestor/painel")
    public String painel(Model model) {
        LocalDate hoje = LocalDate.now();
        model.addAttribute("totalDoadores", doadorRepository.count());
        model.addAttribute("agendamentosPendentes", agendamentoRepository.countByAgeStatus(StatusAgendamento.PENDENTE));
        model.addAttribute("examesEmAberto",
                exameRepository.countByStatusIn(List.of(StatusExame.SOLICITADO, StatusExame.EM_ANALISE)));
        model.addAttribute("niveisEstoque", estoqueService.niveis());
        model.addAttribute("bolsasVencendo", bolsaRepository.findByStatusAndValidadeBetweenOrderByValidadeAsc(
                StatusBolsa.DISPONIVEL, hoje, hoje.plusDays(DIAS_ALERTA_VALIDADE)));
        model.addAttribute("diasAlerta", DIAS_ALERTA_VALIDADE);
        return "gestor/painel";
    }

    /** Relatórios vindos das views do banco (reporting.*), carregados via /api/relatorios/**. */
    @GetMapping("/gestor/relatorios")
    public String relatorios() {
        return "gestor/relatorios";
    }
}
