package com.hemoglobank.api.dto;

import com.hemoglobank.api.model.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Criação de contas de equipe (médico/gestor) por um gestor. */
public record UsuarioNovoDTO(
        @NotBlank(message = "Informe o nome.")
        @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres.")
        String nome,

        @NotBlank(message = "Informe o CPF.")
        @Pattern(regexp = "\\d{11}", message = "O CPF deve ter 11 dígitos, apenas números.")
        String cpf,

        @NotBlank(message = "Informe o e-mail.")
        @Email(message = "E-mail inválido.")
        @Size(max = 120, message = "O e-mail deve ter no máximo 120 caracteres.")
        String email,

        @NotBlank(message = "Informe a senha.")
        @Size(min = 6, max = 72, message = "A senha deve ter entre 6 e 72 caracteres.")
        String senha,

        @NotBlank(message = "Informe o telefone.")
        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
        String telefone,

        @NotNull(message = "Informe o tipo de usuário.")
        TipoUsuario tipoUsuario) {
}
