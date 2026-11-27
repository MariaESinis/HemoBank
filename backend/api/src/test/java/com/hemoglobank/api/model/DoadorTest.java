package com.hemoglobank.api.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoadorTest {

    private final LocalDate hoje = LocalDate.now();

    private Doador doador(String sexo, LocalDate ultimaDoacao) {
        Doador d = new Doador();
        d.setDorSexo(sexo);
        d.setDorDataUltimaDoacao(ultimaDoacao);
        return d;
    }

    @Test
    void quemNuncaDoouPodeDoarHoje() {
        Doador d = doador("M", null);
        assertTrue(d.podeDoarHoje());
        assertEquals(hoje, d.calcularProximaDoacao());
    }

    @Test
    void homemPrecisaEsperar60Dias() {
        assertFalse(doador("M", hoje.minusDays(59)).podeDoarHoje());
        assertTrue(doador("M", hoje.minusDays(60)).podeDoarHoje());
    }

    @Test
    void mulherPrecisaEsperar90DiasMesmoComSexoGravadoComoF() {
        // Regressão: o código antigo comparava com "Feminino", mas o cadastro grava "F".
        assertFalse(doador("F", hoje.minusDays(60)).podeDoarHoje());
        assertFalse(doador("F", hoje.minusDays(89)).podeDoarHoje());
        assertTrue(doador("F", hoje.minusDays(90)).podeDoarHoje());
        assertEquals(90, doador("Feminino", null).intervaloEntreDoacoesEmDias());
    }

    @Test
    void proximaDoacaoNuncaFicaNoPassado() {
        assertEquals(hoje, doador("M", hoje.minusDays(300)).calcularProximaDoacao());
        assertEquals(hoje.plusDays(30), doador("M", hoje.minusDays(30)).calcularProximaDoacao());
    }

    @Test
    void registrarDoacaoSoAvancaADataDaUltimaDoacao() {
        Doador d = doador("M", hoje.minusDays(10));
        d.registrarDoacaoEm(hoje.minusDays(50));
        assertEquals(hoje.minusDays(10), d.getDorDataUltimaDoacao());
        d.registrarDoacaoEm(hoje);
        assertEquals(hoje, d.getDorDataUltimaDoacao());
    }
}
