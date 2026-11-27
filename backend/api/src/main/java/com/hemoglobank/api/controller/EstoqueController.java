package com.hemoglobank.api.controller;

import com.hemoglobank.api.dto.NivelEstoque;
import com.hemoglobank.api.service.EstoqueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Situação pública do estoque por tipagem, calculada a partir das bolsas. */
@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @GetMapping
    public List<NivelEstoque> listarEstoque() {
        return estoqueService.niveis();
    }
}
