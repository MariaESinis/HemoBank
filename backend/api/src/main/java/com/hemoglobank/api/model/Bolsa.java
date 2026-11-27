package com.hemoglobank.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "bolsa")
@Getter
@Setter
public class Bolsa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bol_id")
    private Integer id;

    @Column(name = "bol_tipagem", nullable = false)
    private String tipagem;

    @Enumerated(EnumType.STRING)
    @Column(name = "bol_componente", nullable = false)
    private ComponenteSanguineo componente;

    @Column(name = "bol_quantidade", nullable = false)
    private Integer quantidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "bol_status", nullable = false)
    private StatusBolsa status = StatusBolsa.DISPONIVEL;

    @Column(name = "bol_validade", nullable = false)
    private LocalDate validade;

    // Doador que originou a bolsa
    @ManyToOne
    @JoinColumn(name = "dor_id")
    private Doador doador;
}
