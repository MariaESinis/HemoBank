package com.hemoglobank.api.controller;

import com.hemoglobank.api.exception.RegraNegocioException;
import com.hemoglobank.api.model.Doador;
import com.hemoglobank.api.model.Exame;
import com.hemoglobank.api.model.StatusExame;
import com.hemoglobank.api.repository.AgendamentoRepository;
import com.hemoglobank.api.repository.DoadorRepository;
import com.hemoglobank.api.repository.ExameRepository;
import com.hemoglobank.api.service.AgendamentoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/** Área do doador (/doador/**). O doador é sempre o usuário logado, nunca um id vindo do navegador. */
@Controller
public class DoadorController {

    private static final List<StatusExame> EXAME_EM_ABERTO = Arrays.stream(StatusExame.values())
            .filter(StatusExame::isEmAberto)
            .toList();

    private final DoadorRepository doadorRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final ExameRepository exameRepository;
    private final AgendamentoService agendamentoService;

    public DoadorController(DoadorRepository doadorRepository,
                            AgendamentoRepository agendamentoRepository,
                            ExameRepository exameRepository,
                            AgendamentoService agendamentoService) {
        this.doadorRepository = doadorRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.exameRepository = exameRepository;
        this.agendamentoService = agendamentoService;
    }

    @GetMapping("/doador/painel")
    public String painel(Model model, Principal principal) {
        Doador doador = doadorLogado(principal);
        model.addAttribute("doador", doador);
        model.addAttribute("tiposExame", Exame.TIPOS);
        if (doador != null) {
            model.addAttribute("agendamentos", agendamentoRepository.findByDoadorOrderByAgeDataDescAgeHorarioDesc(doador));
            model.addAttribute("exames", exameRepository.findByDoadorOrderByIdDesc(doador));
            model.addAttribute("proximaDoacao", doador.calcularProximaDoacao());
            model.addAttribute("podeDoar", doador.podeDoarHoje());
        }
        return "doador/painel";
    }

    @GetMapping("/doador/agendamento")
    public String formularioAgendamento(Model model, Principal principal) {
        Doador doador = doadorLogado(principal);
        model.addAttribute("doador", doador);
        model.addAttribute("hoje", LocalDate.now());
        if (doador != null) {
            model.addAttribute("proximaDoacao", doador.calcularProximaDoacao());
            model.addAttribute("podeDoar", doador.podeDoarHoje());
        }
        return "doador/agendamento";
    }

    @PostMapping("/doador/agendamento")
    public String agendar(Principal principal,
                          @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                          @RequestParam("horario") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horario,
                          @RequestParam(value = "observacoes", required = false) String observacoes,
                          RedirectAttributes flash) {
        Doador doador = doadorLogado(principal);
        if (doador == null) {
            flash.addFlashAttribute("erro", "Cadastro de doador não encontrado.");
            return "redirect:/doador/painel";
        }
        try {
            agendamentoService.agendar(doador, data, horario, observacoes);
            flash.addFlashAttribute("sucesso", "Agendamento realizado! Aguarde a confirmação do hemocentro.");
            return "redirect:/doador/painel";
        } catch (RegraNegocioException e) {
            flash.addFlashAttribute("erro", e.getMessage());
            return "redirect:/doador/agendamento";
        }
    }

    @PostMapping("/doador/agendamento/{id}/cancelar")
    public String cancelar(@PathVariable Integer id, Principal principal, RedirectAttributes flash) {
        Doador doador = doadorLogado(principal);
        try {
            if (doador == null) {
                throw new RegraNegocioException("Cadastro de doador não encontrado.");
            }
            agendamentoService.cancelar(id, doador);
            flash.addFlashAttribute("sucesso", "Agendamento cancelado.");
        } catch (RegraNegocioException e) {
            flash.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/doador/painel";
    }

    @PostMapping("/doador/exame/solicitar")
    public String solicitarExame(Principal principal, @RequestParam("tipo") String tipo, RedirectAttributes flash) {
        Doador doador = doadorLogado(principal);
        if (doador == null) {
            flash.addFlashAttribute("erro", "Cadastro de doador não encontrado.");
        } else if (!Exame.TIPOS.contains(tipo)) {
            flash.addFlashAttribute("erro", "Tipo de exame inválido.");
        } else if (exameRepository.existsByDoadorAndTipoAndStatusIn(doador, tipo, EXAME_EM_ABERTO)) {
            flash.addFlashAttribute("erro", "Você já tem um exame \"" + tipo + "\" em andamento.");
        } else {
            Exame exame = new Exame();
            exame.setDoador(doador);
            exame.setTipo(tipo);
            exame.setStatus(StatusExame.SOLICITADO);
            exameRepository.save(exame);
            flash.addFlashAttribute("sucesso", "Exame solicitado com sucesso.");
        }
        return "redirect:/doador/painel";
    }

    private Doador doadorLogado(Principal principal) {
        return principal == null ? null
                : doadorRepository.findByUsuarioEmailIgnoreCase(principal.getName()).orElse(null);
    }
}
