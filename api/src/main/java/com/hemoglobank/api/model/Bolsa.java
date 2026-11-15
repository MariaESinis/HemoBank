package com.hemoglobank.api.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "bolsa")
public class Bolsa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bol_id")
    private Integer id;

    @Column(name = "bol_tipagem")
    private String tipagem; 

    @Column(name = "bol_componente")
    private String componente; 

    @Column(name = "bol_quantidade")
    private Integer quantidade; 

    @Column(name = "bol_status")
    private String status = "DISPONIVEL"; 

    @Column(name = "bol_validade")
    private LocalDate validade;

    // Relacionamento com o Doador (Chave Estrangeira)
    @ManyToOne
    @JoinColumn(name = "dor_id")
    private Doador doador;

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTipagem() { return tipagem; }
    public void setTipagem(String tipagem) { this.tipagem = tipagem; }
    public String getComponente() { return componente; }
    public void setComponente(String componente) { this.componente = componente; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getValidade() { return validade; }
    public void setValidade(LocalDate validade) { this.validade = validade; }
    public Doador getDoador() { return doador; }
    public void setDoador(Doador doador) { this.doador = doador; }
}