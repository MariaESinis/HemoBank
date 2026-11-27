package com.hemoglobank.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usr_id")
    private Long id;

    @Column(name = "usr_nome", nullable = false)
    private String nome;

    @Column(name = "usr_cpf", unique = true, nullable = false)
    private String cpf;

    @Column(name = "usr_email", unique = true, nullable = false)
    private String email;

    // Hash BCrypt. Aceita na entrada (JSON), mas nunca sai em respostas.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "usr_senha", nullable = false)
    private String senha;

    @Column(name = "usr_telefone", nullable = false)
    private String telefone;

    // O perfil de acesso (ROLE_DOADOR, ROLE_MEDICO, ROLE_GESTOR) é derivado deste campo.
    @Enumerated(EnumType.STRING)
    @Column(name = "usr_tipo", nullable = false)
    private TipoUsuario tipoUsuario;

    @Column(name = "usr_data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "usr_sexo_biologico")
    private String sexoBiologico;

    @Column(name = "usr_tipo_sanguineo")
    private String tipoSanguineo;
}
