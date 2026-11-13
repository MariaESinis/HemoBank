package com.hemoglobank.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bolsa")
public class Bolsa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bolId;

    private LocalDateTime bolDataGerada;
    private Integer bolQuantidadeMl;
    private String bolTipo;
    private String bolObservacoes;
    private String bolTipagem;
    private LocalDateTime bolValidade;

    @ManyToOne
    @JoinColumn(name = "doa_id")
    private Doacao doacao;

    @ManyToOne
    @JoinColumn(name = "est_id")
    private Estoque estoque;

    // Getters e Setters
    public Integer getBolId() { return bolId; }
    public void setBolId(Integer bolId) { this.bolId = bolId; }
    public LocalDateTime getBolDataGerada() { return bolDataGerada; }
    public void setBolDataGerada(LocalDateTime bolDataGerada) { this.bolDataGerada = bolDataGerada; }
    public Integer getBolQuantidadeMl() { return bolQuantidadeMl; }
    public void setBolQuantidadeMl(Integer bolQuantidadeMl) { this.bolQuantidadeMl = bolQuantidadeMl; }
    public String getBolTipo() { return bolTipo; }
    public void setBolTipo(String bolTipo) { this.bolTipo = bolTipo; }
    public String getBolObservacoes() { return bolObservacoes; }
    public void setBolObservacoes(String bolObservacoes) { this.bolObservacoes = bolObservacoes; }
    public String getBolTipagem() { return bolTipagem; }
    public void setBolTipagem(String bolTipagem) { this.bolTipagem = bolTipagem; }
    public LocalDateTime getBolValidade() { return bolValidade; }
    public void setBolValidade(LocalDateTime bolValidade) { this.bolValidade = bolValidade; }
    public Doacao getDoacao() { return doacao; }
    public void setDoacao(Doacao doacao) { this.doacao = doacao; }
    public Estoque getEstoque() { return estoque; }
    public void setEstoque(Estoque estoque) { this.estoque = estoque; }
}