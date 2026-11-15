package com.hemoglobank.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "doador")
@Getter
@Setter
public class Doador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dor_id")
    private Integer dorId;

    @Column(name = "dor_nome", nullable = false)
    private String dorNome;

    @Column(name = "dor_cpf", unique = true, nullable = false)
    private String dorCpf;

    @Column(name = "dor_data_nascimento")
    private LocalDate dorDataNascimento;

    @Column(name = "dor_sexo")
    private String dorSexo;

    @Column(name = "dor_telefone")
    private String dorTelefone;

    @Column(name = "dor_email")
    private String dorEmail;

    @Column(name = "dor_created_at")
    private LocalDateTime dorCreatedAt;

    @Column(name = "dor_updated_at")
    private LocalDateTime dorUpdatedAt;

    // Relacionamento com a entidade Usuario (chave estrangeira usr_id)
    @ManyToOne
    @JoinColumn(name = "usr_id")
    private Usuario usuario;

    // Getters e Setters manuais para garantir a compatibilidade do relacionamento com o Usuario
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

 // Atributo para armazenar a data da última doação (necessário para o cálculo)
    @Column(name = "dor_data_ultima_doacao")
    private LocalDate dorDataUltimaDoacao;

    public LocalDate getDorDataUltimaDoacao() {
        return dorDataUltimaDoacao;
    }

    public void setDorDataUltimaDoacao(LocalDate dorDataUltimaDoacao) {
        this.dorDataUltimaDoacao = dorDataUltimaDoacao;
    }

    public LocalDate calcularProximaDoacao() {
        // Se nunca doou, pode doar hoje (ou na data de cadastro)
        if (this.dorDataUltimaDoacao == null) {
            return LocalDate.now();
        }
        
        // Intervalo padrão: 60 dias para Masculino, 90 dias para Feminino (conforme regras do Ministério da Saúde)
        int diasIntervalo = "Feminino".equalsIgnoreCase(this.dorSexo) ? 90 : 60;
        
        return this.dorDataUltimaDoacao.plusDays(diasIntervalo);
    }

    public boolean podeDoarHoje() {
        LocalDate proxima = calcularProximaDoacao();
        return LocalDate.now().isAfter(proxima) || LocalDate.now().isEqual(proxima);
    }
}