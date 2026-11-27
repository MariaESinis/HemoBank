package com.hemoglobank.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "exame")
@Getter
@Setter
public class Exame {

    /** Tipos de exame que o doador pode solicitar. */
    public static final List<String> TIPOS = List.of(
            "Hemograma Completo",
            "Sorologia (HIV/Hepatites)",
            "Tipagem Sanguínea",
            "Glicemia");

    public static final int RESULTADO_MAX = 500;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exm_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "dor_id")
    private Doador doador;

    @Column(name = "exm_tipo", nullable = false)
    private String tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "exm_status", nullable = false)
    private StatusExame status = StatusExame.SOLICITADO;

    @Column(name = "exm_resultado", length = RESULTADO_MAX)
    private String resultado;

    @Column(name = "exm_data_solicitacao")
    private LocalDateTime dataSolicitacao;

    @PrePersist
    void antesDeSalvar() {
        if (dataSolicitacao == null) {
            dataSolicitacao = LocalDateTime.now();
        }
    }
}
