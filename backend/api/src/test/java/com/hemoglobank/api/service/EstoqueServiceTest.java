package com.hemoglobank.api.service;

import com.hemoglobank.api.dto.NivelEstoque.Nivel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EstoqueServiceTest {

    @Test
    void classificaOsNiveisPelosLimites() {
        assertEquals(Nivel.CRITICO, EstoqueService.classificar(0));
        assertEquals(Nivel.CRITICO, EstoqueService.classificar(2));
        assertEquals(Nivel.ESTAVEL, EstoqueService.classificar(3));
        assertEquals(Nivel.ESTAVEL, EstoqueService.classificar(9));
        assertEquals(Nivel.ABASTECIDO, EstoqueService.classificar(10));
        assertEquals(Nivel.ABASTECIDO, EstoqueService.classificar(50));
    }

    @Test
    void cobreAsOitoTipagens() {
        assertEquals(8, EstoqueService.TIPAGENS.size());
    }
}
