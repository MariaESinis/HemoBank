package com.hemoglobank.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "doacao")
public class Doacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer doaId;

    private LocalDateTime doaDataDoacao;
    private Integer doaQuantidadeMl;
    private String doaTipoDoacao;
    private String doaObservacoes;
    private String doaTipagem;
    private String doaTipo;
    private LocalDateTime doaValidade;

    @ManyToOne
    @JoinColumn(name = "dor_id")
    private Doador doador;

    // Getters e Setters
    public Integer getDoaId() { return doaId; }
    public void setDoaId(Integer doaId) { this.doaId = doaId; }
    public LocalDateTime getDoaDataDoacao() { return doaDataDoacao; }
    public void setDoaDataDoacao(LocalDateTime doaDataDoacao) { this.doaDataDoacao = doaDataDoacao; }
    public Integer getDoaQuantidadeMl() { return doaQuantidadeMl; }
    public void setDoaQuantidadeMl(Integer doaQuantidadeMl) { this.doaQuantidadeMl = doaQuantidadeMl; }
    public String getDoaTipoDoacao() { return doaTipoDoacao; }
    public void setDoaTipoDoacao(String doaTipoDoacao) { this.doaTipoDoacao = doaTipoDoacao; }
    public String getDoaObservacoes() { return doaObservacoes; }
    public void setDoaObservacoes(String doaObservacoes) { this.doaObservacoes = doaObservacoes; }
    public String getDoaTipagem() { return doaTipagem; }
    public void setDoaTipagem(String doaTipagem) { this.doaTipagem = doaTipagem; }
    public String getDoaTipo() { return doaTipo; }
    public void setDoaTipo(String doaTipo) { this.doaTipo = doaTipo; }
    public LocalDateTime getDoaValidade() { return doaValidade; }
    public void setDoaValidade(LocalDateTime doaValidade) { this.doaValidade = doaValidade; }
    public Doador getDoador() { return doador; }
    public void setDoador(Doador doador) { this.doador = doador; }
}