package com.hemoglobank.api.controller;

import com.hemoglobank.api.model.Usuario;
import com.hemoglobank.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;


    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Usuario usuario) {
        try {
            // Criptografa a senha antes de salvar
            usuario.setSenha(encoder.encode(usuario.getSenha()));
            Usuario salvo = repository.save(usuario);
            salvo.setSenha(null); // nunca devolve o hash da senha na resposta
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (DataIntegrityViolationException e) {
            // Log da causa raiz no console pra facilitar debug (ex.: NOT NULL, coluna
            // única duplicada, etc). Antes isso era engolido e sempre virava "e-mail duplicado".
            String causa = e.getMostSpecificCause().getMessage();
            System.err.println("Falha ao cadastrar usuário: " + causa);

            // Ainda respondemos 409, mas agora com a mensagem real pro front exibir/debugar
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", causa));
        }
    }

    @GetMapping
    public List<Usuario> listar() {
        List<Usuario> usuarios = repository.findAll();
        usuarios.forEach(u -> u.setSenha(null)); // não expõe hashes de senha na listagem
        return usuarios;
    }
}