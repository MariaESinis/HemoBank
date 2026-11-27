package com.hemoglobank.api.dto;

import com.hemoglobank.api.model.TipoUsuario;
import com.hemoglobank.api.model.Usuario;

public record UsuarioResumoDTO(Long id, String nome, String email, TipoUsuario tipoUsuario) {

    public static UsuarioResumoDTO de(Usuario u) {
        return new UsuarioResumoDTO(u.getId(), u.getNome(), u.getEmail(), u.getTipoUsuario());
    }
}
