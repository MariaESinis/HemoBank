package com.hemoglobank.api.controller;

import com.hemoglobank.api.exception.RegraNegocioException;
import com.hemoglobank.api.model.ComponenteSanguineo;
import com.hemoglobank.api.model.Exame;
import com.hemoglobank.api.model.StatusAgendamento;
import com.hemoglobank.api.model.StatusExame;
import com.hemoglobank.api.repository.AgendamentoRepository;
import com.hemoglobank.api.repository.BolsaRepository;
import com.hemoglobank.api.repository.ExameRepository;
import com.hemoglobank.api.service.AgendamentoService;
import com.hemoglobank.api.service.BolsaService;
import com.hemoglobank.api.service.EstoqueService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/** Área do médico (/medico/**). */
@Controller
public class MedicoController {

    private final AgendamentoRepository agendamentoRepository;
    private final ExameRepository exameRepository;
    private final BolsaRepository bolsaRepository;
    private final AgendamentoService agendamentoService;
    private final BolsaService bolsaService;

    public MedicoController(AgendamentoRepository agendamentoRepository,
                            ExameRepository exameRepository,
                            BolsaRepository bolsaRepository,
                            AgendamentoService agendamentoService,
                            BolsaService bolsaService) {
        this.agendamentoRepository = agendamentoRepository;
        this.exameRepository = exameRepository;
        this.bolsaRepository = bolsaRepository;
        this.agendamentoService = agendamentoService;
        this.bolsaService = bolsaService;
    }

    @GetMapping("/medico/painel")
    public String painel(Model model) {
        model.addAttribute("agendamentos", agendamentoRepository.findAllByOrderByAgeDataAscAgeHorarioAsc());
        model.addAttribute("exames", exameRepository.findAllByOrderByIdDesc());
        model.addAttribute("bolsas", bolsaRepository.findTop10ByOrderByIdDesc());
        model.addAttribute("statusAgendamento", StatusAgendamento.values());
        model.addAttribute("statusExame", StatusExame.values());
        model.addAttribute("componentes", ComponenteSanguineo.values());
        model.addAttribute("tipagens", EstoqueService.TIPAGENS);
        model.addAttribute("hoje", LocalDate.now());
        return "medico/painel";
    }

    @PostMapping("/medico/exame/atualizar/{id}")
    public String atualizarExame(@PathVariable Integer id,
                                 @RequestParam("status") StatusExame status,
                                 @RequestParam(value = "resultado", required = false) String resultado,
                                 RedirectAttributes flash) {
        String texto = resultado == null || resultado.isBlank() ? null : resultado.trim();
        if (texto != null && texto.length() > Exame.RESULTADO_MAX) {
            flash.addFlashAttribute("erro", "O resultado deve ter no máximo " + Exame.RESULTADO_MAX + " caracteres.");
            return "redirect:/medico/painel";
        }

        exameRepository.findById(id).ifPresentOrElse(exame -> {
            exame.setStatus(status);
            exame.setResultado(texto);
            exameRepository.save(exame);
            flash.addFlashAttribute("sucesso", "Exame atualizado.");
        }, () -> flash.addFlashAttribute("erro", "Exame não encontrado."));
        return "redirect:/medico/painel";
    }

    @PostMapping("/medico/agendamento/atualizar/{id}")
    public String atualizarAgendamento(@PathVariable Integer id,
                                       @RequestParam("status") StatusAgendamento status,
                                       @RequestParam(value = "observacoes", required = false) String observacoes,
                                       RedirectAttributes flash) {
        try {
            agendamentoService.atualizar(id, status, observacoes);
            flash.addFlashAttribute("sucesso", "Agendamento atualizado.");
        } catch (RegraNegocioException e) {
            flash.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/medico/painel";
    }

    @PostMapping("/medico/bolsa/cadastrar")
    public String cadastrarBolsa(@RequestParam("cpfDoador") String cpfDoador,
                                 @RequestParam("tipagem") String tipagem,
                                 @RequestParam("componente") ComponenteSanguineo componente,
                                 @RequestParam("quantidade") Integer quantidade,
                                 @RequestParam("validade") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate validade,
                                 RedirectAttributes flash) {
        try {
            bolsaService.cadastrar(cpfDoador, tipagem, componente, quantidade, validade);
            flash.addFlashAttribute("sucesso", "Bolsa cadastrada e doação registrada para o doador.");
        } catch (RegraNegocioException e) {
            flash.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/medico/painel";
    }
}
