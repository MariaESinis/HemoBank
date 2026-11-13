package com.hemoglobank.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hemoglobank.api.model.Doador;
import com.hemoglobank.api.model.Usuario;
import com.hemoglobank.api.repository.AgendamentoRepository;
import com.hemoglobank.api.repository.DoadorRepository;
import com.hemoglobank.api.repository.UsuarioRepository;

@Controller
public class PageController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private DoadorRepository doadorRepository;

    // Página Inicial
    @GetMapping("/")
    public String index() {
        return "index"; 
    }

    @GetMapping("/cadastro")
    public String paginaCadastro() {
        return "cadastro"; 
    }

    // Página de Login
    @GetMapping("/login")
    public String login() {
        return "login"; 
    }

    @GetMapping("/doador/painel")
    public String painelDoador(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername()).orElse(null);
            model.addAttribute("usuario", usuario);
            
            if (usuario != null) {
                // Busca o doador vinculado ao usuário e seus agendamentos
                Doador doador = doadorRepository.findByUsuario(usuario).orElse(null);
                if (doador != null) {
                    model.addAttribute("agendamentos", agendamentoRepository.findByDoador(doador));
                }
            }
        }
        return "doador/painel";
    }

    @GetMapping("/medico/painel")
    public String painelMedico(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername()).orElse(null);
            model.addAttribute("usuario", usuario);
        }
        // O médico pode ver todos os agendamentos cadastrados no sistema
        model.addAttribute("agendamentos", agendamentoRepository.findAll());
        return "medico/painel";
    }

    @GetMapping("/doador/agendamento")
    public String agendamentoDoador() {
        return "doador/agendamento"; 
    }

    @GetMapping("/medico/exames")
    public String exameMedico() {
        return "medico/exame"; 
    }
}