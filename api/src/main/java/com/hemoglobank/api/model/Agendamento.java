package com.hemoglobank.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento")
public class Agendamento {

   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "age_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "dor_id")
    private Doador doador;

    @Column(name = "age_data")
    private String ageData;

    @Column(name = "age_horario")
    private String ageHorario;

    @Column(name = "age_observacoes")
    private String ageObservacoes;

    // Adicione um campo de status se ainda não tiver:
    @Column(name = "age_status")
    private String ageStatus = "PENDENTE"; // Padrão inicial

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Doador getDoador() { return doador; }
    public void setDoador(Doador doador) { this.doador = doador; }

    public String getAgeData() { return ageData; }
    public void setAgeData(String ageData) { this.ageData = ageData; }

    public String getAgeHorario() { return ageHorario; }
    public void setAgeHorario(String ageHorario) { this.ageHorario = ageHorario; }

    public String getAgeObservacoes() { return ageObservacoes; }
    public void setAgeObservacoes(String ageObservacoes) { this.ageObservacoes = ageObservacoes; }

    public String getAgeStatus() { return ageStatus; }
    public void setAgeStatus(String ageStatus) { this.ageStatus = ageStatus; }
}