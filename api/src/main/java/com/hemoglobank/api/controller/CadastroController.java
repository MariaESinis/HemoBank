package com.hemoglobank.api.controller;

import com.hemoglobank.api.model.Doador;
import com.hemoglobank.api.model.TipoUsuario;
import com.hemoglobank.api.model.Usuario;
import com.hemoglobank.api.repository.DoadorRepository;
import com.hemoglobank.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/cadastro")
public class CadastroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DoadorRepository doadorRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/doador")
    public ResponseEntity<?> cadastrarDoador(@RequestBody DoadorRegistroDTO dto) {
        try {
            // 1. Cria e salva o Usuário de acesso
            Usuario usuario = new Usuario();
            usuario.setNome(dto.getNome());
            usuario.setCpf(dto.getCpf());
            usuario.setTelefone(dto.getTelefone());
            usuario.setEmail(dto.getEmail());
            usuario.setSenha(encoder.encode(dto.getSenha()));
            usuario.setRole("ROLE_DOADOR"); 
            usuario.setTipoUsuario(TipoUsuario.DOADOR);

            // Salva o usuário no banco para obter a instância persistida
            Usuario usuarioSalvo = usuarioRepository.save(usuario);

          // 2. Cria o Doador vinculado ao Usuário recém-criado
Doador doador = new Doador();
doador.setDorNome(dto.getNome());
doador.setDorCpf(dto.getCpf());

// Conversão de LocalDateTime para LocalDate aqui:
if (dto.getDataNascimento() != null) {
    doador.setDorDataNascimento(dto.getDataNascimento().toLocalDate());
}

doador.setDorSexo(dto.getSexo());
doador.setDorTelefone(dto.getTelefone());
doador.setDorEmail(dto.getEmail());
doador.setUsuario(usuarioSalvo); 
doador.setDorCreatedAt(LocalDateTime.now());
doador.setDorUpdatedAt(LocalDateTime.now());
            Doador doadorSalvo = doadorRepository.save(doador);
            return ResponseEntity.status(201).body(doadorSalvo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao realizar cadastro: " + e.getMessage());
        }
    }

    public static class DoadorRegistroDTO {
        private String nome;
        private String email;
        private String senha;
        private String cpf;
        private LocalDateTime dataNascimento;
        private String sexo;
        private String telefone;

        // Getters e Setters
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
        public String getCpf() { return cpf; }
        public void setCpf(String cpf) { this.cpf = cpf; }
        public LocalDateTime getDataNascimento() { return dataNascimento; }
        public void setDataNascimento(LocalDateTime dataNascimento) { this.dataNascimento = dataNascimento; }
        public String getSexo() { return sexo; }
        public void setSexo(String sexo) { this.sexo = sexo; }
        public String getTelefone() { return telefone; }
        public void setTelefone(String telefone) { this.telefone = telefone; }
    }
}