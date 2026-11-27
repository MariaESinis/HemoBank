package com.hemoglobank.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "agendamento")
@Getter
@Setter
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "age_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "dor_id")
    private Doador doador;

    @Column(name = "age_data", nullable = false)
    private LocalDate ageData;

    @Column(name = "age_horario", nullable = false)
    private LocalTime ageHorario;

    @Column(name = "age_observacoes", length = 500)
    private String ageObservacoes;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_status", nullable = false)
    private StatusAgendamento ageStatus = StatusAgendamento.PENDENTE;
}
