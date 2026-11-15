package com.hemoglobank.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "estoque")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer estId;

    private String estTipagem;
    private String estTipo;
    private String estStatus;
    private Integer estQuantidade;
    
    private LocalDateTime estCreatedAt;
    private LocalDateTime estUpdatedAt;

    // Getters e Setters
    public Integer getEstId() { return estId; }
    public void setEstId(Integer estId) { this.estId = estId; }
    public String getEstTipagem() { return estTipagem; }
    public void setEstTipagem(String estTipagem) { this.estTipagem = estTipagem; }
    public String getEstTipo() { return estTipo; }
    public void setEstTipo(String estTipo) { this.estTipo = estTipo; }
    public String getEstStatus() { return estStatus; }
    public void setEstStatus(String estStatus) { this.estStatus = estStatus; }
    public Integer getEstQuantidade() { return estQuantidade; }
    public void setEstQuantidade(Integer estQuantidade) { this.estQuantidade = estQuantidade; }
    public LocalDateTime getEstCreatedAt() { return estCreatedAt; }
    public void setEstCreatedAt(LocalDateTime estCreatedAt) { this.estCreatedAt = estCreatedAt; }
    public LocalDateTime getEstUpdatedAt() { return estUpdatedAt; }
    public void setEstUpdatedAt(LocalDateTime estUpdatedAt) { this.estUpdatedAt = estUpdatedAt; }
}