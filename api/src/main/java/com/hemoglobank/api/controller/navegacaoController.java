package com.hemoglobank.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class navegacaoController {
    //rotas publicas
    @GetMapping("/hemocentro")
    public String home(){
        return "index";
    }


    @GetMapping("/hemocentro/cad")
    public String cadastro(){
        return "cadastro";
    }

    @GetMapping("/hemocentro/login")
    public String login(){
        return "login";
    }

    //rotas privadas baseada no role
    @GetMapping("/hemocentro/doador")
    public String painelDoador(){
        return "doador/painel";
    }

    @GetMapping("/hemocentro/medico")
    public String painelMedico(){
        return "medico/painel";
    }
    @GetMapping("/hemocentro/gestor")
    public String painelGestor() {
        return "gestor/painel"; // Procura por templates/gestor/painel.html
    }


}