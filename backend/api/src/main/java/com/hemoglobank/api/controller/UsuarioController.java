package com.hemoglobank.api.controller;

import com.hemoglobank.api.dto.UsuarioNovoDTO;
import com.hemoglobank.api.dto.UsuarioResumoDTO;
import com.hemoglobank.api.model.TipoUsuario;
import com.hemoglobank.api.model.Usuario;
import com.hemoglobank.api.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Administração de contas da equipe. Acesso restrito ao perfil GESTOR (ver SecurityConfig). */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public UsuarioController(UsuarioRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody UsuarioNovoDTO dto, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, String> campos = new LinkedHashMap<>();
            resultado.getFieldErrors().forEach(e -> campos.putIfAbsent(e.getField(), e.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of("erro", "Dados inválidos.", "campos", campos));
        }
        // Contas de doador nascem pelo cadastro público (que também cria o registro do doador)
        if (dto.tipoUsuario() == TipoUsuario.DOADOR) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Use o cadastro público para criar contas de doador."));
        }

        String email = dto.email().trim().toLowerCase();
        if (repository.existsByEmailIgnoreCase(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", "Já existe uma conta com este e-mail."));
        }
        if (repository.existsByCpf(dto.cpf())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", "Já existe uma conta com este CPF."));
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome().trim());
        usuario.setCpf(dto.cpf());
        usuario.setEmail(email);
        usuario.setTelefone(dto.telefone().trim());
        usuario.setSenha(encoder.encode(dto.senha()));
        usuario.setTipoUsuario(dto.tipoUsuario());

        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResumoDTO.de(repository.save(usuario)));
    }

    @GetMapping
    public List<UsuarioResumoDTO> listar() {
        return repository.findAll().stream().map(UsuarioResumoDTO::de).toList();
    }
}
