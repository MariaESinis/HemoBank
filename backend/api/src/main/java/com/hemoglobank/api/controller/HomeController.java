package com.hemoglobank.api.controller;

import com.hemoglobank.api.service.EstoqueService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** Páginas públicas. */
@Controller
public class HomeController {

    private final EstoqueService estoqueService;

    public HomeController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("niveisEstoque", estoqueService.niveis());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro";
    }
}
