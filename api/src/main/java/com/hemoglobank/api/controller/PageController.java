package com.hemoglobank.api.controller;

import java.time.LocalDate;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hemoglobank.api.model.Doador;
import com.hemoglobank.api.model.Exame;
import com.hemoglobank.api.model.Bolsa; // Ajuste para o nome correto da sua model de bolsa
import com.hemoglobank.api.model.Usuario;
import com.hemoglobank.api.repository.AgendamentoRepository;
import com.hemoglobank.api.repository.BolsaRepository;
import com.hemoglobank.api.repository.DoadorRepository;
import com.hemoglobank.api.repository.ExameRepository;
import com.hemoglobank.api.repository.UsuarioRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@Controller
public class PageController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private DoadorRepository doadorRepository;

    @Autowired
    private ExameRepository exameRepository;

    @Autowired
    private BolsaRepository bolsaRepository;

    // Página Inicial com Estoque Dinâmico (Unificada)
    @GetMapping("/")
public String index(Model model) {
    List<String> tiposSanguineos = List.of("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-");
    Map<String, String> niveisEstoque = new LinkedHashMap<>();

    for (String tipo : tiposSanguineos) {
        long qtd = bolsaRepository.countByTipagemAndStatus(tipo, "DISPONIVEL");
        
        if (qtd < 3) {
            niveisEstoque.put(tipo, "CRÍTICO");
        } else if (qtd < 10) {
            niveisEstoque.put(tipo, "ESTÁVEL");
        } else {
            niveisEstoque.put(tipo, "ABASTECIDO");
        }
    }

    model.addAttribute("niveisEstoque", niveisEstoque);
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

    // Painel do Doador
   @GetMapping("/doador/painel")
public String painelDoador(Model model, @AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails != null) {
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername()).orElse(null);
        model.addAttribute("usuario", usuario);
        
        if (usuario != null) {
            Doador doador = doadorRepository.findByUsuario(usuario).orElse(null);
            if (doador != null) {
                model.addAttribute("doador", doador);
                model.addAttribute("agendamentos", agendamentoRepository.findByDoador(doador));
                model.addAttribute("exames", exameRepository.findByDoador(doador));
                
                // Adiciona o cálculo da próxima doação e status de aptidão
                model.addAttribute("proximaDoacao", doador.calcularProximaDoacao());
                model.addAttribute("podeDoar", doador.podeDoarHoje());
            }
        }
    }
    return "doador/painel";
}

    // Rota para o doador enviar o pedido de exame
    @PostMapping("/doador/exame/solicitar")
    public String solicitarExame(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("tipo") String tipo) {
        if (userDetails != null) {
            Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername()).orElse(null);
            if (usuario != null) {
                Doador doador = doadorRepository.findByUsuario(usuario).orElse(null);
                if (doador != null) {
                    Exame exame = new Exame();
                    exame.setDoador(doador);
                    exame.setTipo(tipo);
                    exame.setStatus("SOLICITADO");
                    exameRepository.save(exame);
                }
            }
        }
        return "redirect:/doador/painel";
    }

    // Painel do Médico
    @GetMapping("/medico/painel")
    public String painelMedico(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername()).orElse(null);
            model.addAttribute("usuario", usuario);
        }
        model.addAttribute("agendamentos", agendamentoRepository.findAll());
        model.addAttribute("exames", exameRepository.findAll()); 
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

    // Atualizar Exame (Médico)
    @PostMapping("/medico/exame/atualizar/{id}")
    public String atualizarExame(
            @PathVariable Integer id,
            @RequestParam("status") String status,
            @RequestParam(value = "resultado", required = false) String resultado) {

        Exame exame = exameRepository.findById(id).orElse(null);
        if (exame != null) {
            exame.setStatus(status);
            exame.setResultado(resultado);
            exameRepository.save(exame);
        }
        return "redirect:/medico/painel";
    }


@PostMapping("/medico/bolsa/cadastrar")
public String cadastrarBolsa(
        @RequestParam("cpfDoador") String cpfDoador,
        @RequestParam("tipagem") String tipagem,
        @RequestParam("componente") String componente,
        @RequestParam("quantidade") Integer quantidade,
        @RequestParam("validade") LocalDate validade) {
    
    // Busca o doador pelo CPF cadastrado
   Doador doador = doadorRepository.findByDorCpf(cpfDoador).orElse(null);
    
    if (doador != null) {
        Bolsa bolsa = new Bolsa();
        bolsa.setTipagem(tipagem);
        bolsa.setComponente(componente);
        bolsa.setQuantidade(quantidade);
        bolsa.setValidade(validade);
        bolsa.setStatus("DISPONIVEL");
        bolsa.setDoador(doador); // Associa o doador à bolsa!
        
        bolsaRepository.save(bolsa);
    }
    
    return "redirect:/medico/painel";
}
}