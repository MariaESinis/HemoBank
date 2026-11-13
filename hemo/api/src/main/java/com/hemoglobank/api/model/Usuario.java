package com.hemoglobank.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class Usuario implements UserDetails {

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

    @Column(name = "usr_senha", nullable = false)
    private String senha;

    @Column(name = "usr_telefone", nullable = false)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(name = "usr_tipo", nullable = false)
    private TipoUsuario tipoUsuario;

    // --- Campos específicos para DOADORES ---
    @Column(name = "usr_data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "usr_sexo_biologico")
    private String sexoBiologico;

    @Column(name = "usr_tipo_sanguineo")
    private String tipoSanguineo;
    private String role; // ou perfil

public String getRole() {
    return role;
}

public void setRole(String role) {
    this.role = role;
}

    // ==========================================
    // METODOS DA INTERFACE USERDETAILS
    // ==========================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Converte o TipoUsuario (DOADOR, MEDICO, GESTOR) em ROLE para o Security
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.tipoUsuario.name()));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email; // Usaremos o email para fazer login
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}