package com.hemoglobank.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exame")
public class Exame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer exaId;

    private String exaTipo;
    private String exaResultado;
    private LocalDateTime exaData;
    private String exaObservacoes;

    @ManyToOne
    @JoinColumn(name = "doa_id")
    private Doacao doacao;

    // Getters e Setters
    public Integer getExaId() { return exaId; }
    public void setExaId(Integer exaId) { this.exaId = exaId; }
    public String getExaTipo() { return exaTipo; }
    public void setExaTipo(String exaTipo) { this.exaTipo = exaTipo; }
    public String getExaResultado() { return exaResultado; }
    public void setExaResultado(String exaResultado) { this.exaResultado = exaResultado; }
    public LocalDateTime getExaData() { return exaData; }
    public void setExaData(LocalDateTime exaData) { this.exaData = exaData; }
    public String getExaObservacoes() { return exaObservacoes; }
    public void setExaObservacoes(String exaObservacoes) { this.exaObservacoes = exaObservacoes; }
    public Doacao getDoacao() { return doacao; }
    public void setDoacao(Doacao doacao) { this.doacao = doacao; }
}