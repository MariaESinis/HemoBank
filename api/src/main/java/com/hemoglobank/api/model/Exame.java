package com.hemoglobank.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "exame")
public class Exame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exm_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "dor_id")
    private Doador doador;

    @Column(name = "exm_tipo")
    private String tipo;

    @Column(name = "exm_status")
    private String status = "SOLICITADO"; // Padrão inicial

    @Column(name = "exm_resultado")
private String resultado;

// Getter e Setter
public String getResultado() { return resultado; }
public void setResultado(String resultado) { this.resultado = resultado; }
    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Doador getDoador() { return doador; }
    public void setDoador(Doador doador) { this.doador = doador; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}