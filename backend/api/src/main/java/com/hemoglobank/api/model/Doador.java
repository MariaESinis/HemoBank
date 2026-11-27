package com.hemoglobank.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Table(name = "doador")
@Getter
@Setter
public class Doador {

    /** Intervalo mínimo entre doações de sangue total (regra do Ministério da Saúde). */
    public static final int INTERVALO_HOMEM_DIAS = 60;
    public static final int INTERVALO_MULHER_DIAS = 90;

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

    /** "M", "F" ou "Outro". */
    @Column(name = "dor_sexo")
    private String dorSexo;

    @Column(name = "dor_telefone")
    private String dorTelefone;

    @Column(name = "dor_email")
    private String dorEmail;

    @Column(name = "dor_data_ultima_doacao")
    private LocalDate dorDataUltimaDoacao;

    @Column(name = "dor_created_at")
    private LocalDateTime dorCreatedAt;

    @Column(name = "dor_updated_at")
    private LocalDateTime dorUpdatedAt;

    // Conta de acesso do doador. Nunca é serializada em JSON.
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "usr_id")
    private Usuario usuario;

    @PrePersist
    void antesDeInserir() {
        LocalDateTime agora = LocalDateTime.now();
        if (dorCreatedAt == null) dorCreatedAt = agora;
        if (dorUpdatedAt == null) dorUpdatedAt = agora;
    }

    @PreUpdate
    void antesDeAtualizar() {
        dorUpdatedAt = LocalDateTime.now();
    }

    // ---------------- Regras de negócio ----------------

    public boolean ehFeminino() {
        return "F".equalsIgnoreCase(dorSexo) || "Feminino".equalsIgnoreCase(dorSexo);
    }

    public int intervaloEntreDoacoesEmDias() {
        return ehFeminino() ? INTERVALO_MULHER_DIAS : INTERVALO_HOMEM_DIAS;
    }

    /** Primeira data em que o doador pode doar (nunca anterior a hoje). */
    public LocalDate calcularProximaDoacao() {
        LocalDate hoje = LocalDate.now();
        if (dorDataUltimaDoacao == null) {
            return hoje;
        }
        LocalDate liberada = dorDataUltimaDoacao.plusDays(intervaloEntreDoacoesEmDias());
        return liberada.isAfter(hoje) ? liberada : hoje;
    }

    public boolean podeDoarHoje() {
        return !calcularProximaDoacao().isAfter(LocalDate.now());
    }

    public int calcularIdade() {
        return dorDataNascimento == null ? 0 : Period.between(dorDataNascimento, LocalDate.now()).getYears();
    }

    /** Registra uma doação; só avança a data (nunca volta no tempo). */
    public void registrarDoacaoEm(LocalDate data) {
        if (data != null && (dorDataUltimaDoacao == null || data.isAfter(dorDataUltimaDoacao))) {
            dorDataUltimaDoacao = data;
        }
    }
}
