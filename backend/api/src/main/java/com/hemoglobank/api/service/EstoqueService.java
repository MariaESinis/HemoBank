package com.hemoglobank.api.service;

import com.hemoglobank.api.dto.NivelEstoque;
import com.hemoglobank.api.dto.NivelEstoque.Nivel;
import com.hemoglobank.api.model.StatusBolsa;
import com.hemoglobank.api.repository.BolsaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Calcula o estoque a partir das bolsas (única fonte de verdade). */
@Service
public class EstoqueService {

    public static final List<String> TIPAGENS = List.of("O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-");

    /** Menos que isto = crítico. */
    static final int LIMITE_CRITICO = 3;
    /** Menos que isto (e a partir do crítico) = estável; acima = abastecido. */
    static final int LIMITE_ESTAVEL = 10;

    private final BolsaRepository bolsaRepository;

    public EstoqueService(BolsaRepository bolsaRepository) {
        this.bolsaRepository = bolsaRepository;
    }

    @Transactional(readOnly = true)
    public List<NivelEstoque> niveis() {
        LocalDate hoje = LocalDate.now();
        List<NivelEstoque> niveis = new ArrayList<>();
        for (String tipagem : TIPAGENS) {
            // Bolsas vencidas não contam como estoque
            long qtd = bolsaRepository.countByTipagemAndStatusAndValidadeGreaterThanEqual(
                    tipagem, StatusBolsa.DISPONIVEL, hoje);
            niveis.add(new NivelEstoque(tipagem, qtd, classificar(qtd)));
        }
        return niveis;
    }

    static Nivel classificar(long bolsas) {
        if (bolsas < LIMITE_CRITICO) return Nivel.CRITICO;
        if (bolsas < LIMITE_ESTAVEL) return Nivel.ESTAVEL;
        return Nivel.ABASTECIDO;
    }
}
