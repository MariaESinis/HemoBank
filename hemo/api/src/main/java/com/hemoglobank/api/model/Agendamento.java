package com.hemoglobank.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ageId;

    private LocalDateTime ageData;
    private LocalDateTime ageHorario;
    private String ageObservacoes;

    @ManyToOne
    @JoinColumn(name = "dor_id")
    private Doador doador;

    // Getters e Setters
    public Integer getAgeId() { return ageId; }
    public void setAgeId(Integer ageId) { this.ageId = ageId; }
    public LocalDateTime getAgeData() { return ageData; }
    public void setAgeData(LocalDateTime ageData) { this.ageData = ageData; }
    public LocalDateTime getAgeHorario() { return ageHorario; }
    public void setAgeHorario(LocalDateTime ageHorario) { this.ageHorario = ageHorario; }
    public String getAgeObservacoes() { return ageObservacoes; }
    public void setAgeObservacoes(String ageObservacoes) { this.ageObservacoes = ageObservacoes; }
    public Doador getDoador() { return doador; }
    public void setDoador(Doador doador) { this.doador = doador; }
}