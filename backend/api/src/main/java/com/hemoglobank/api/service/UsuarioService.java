package com.hemoglobank.api.service;

import com.hemoglobank.api.model.Usuario;
import com.hemoglobank.api.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** Carrega o usuário (login por e-mail) para o Spring Security. */
@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                // DOADOR -> ROLE_DOADOR, MEDICO -> ROLE_MEDICO, GESTOR -> ROLE_GESTOR
                .roles(usuario.getTipoUsuario().name())
                .build();
    }
}
