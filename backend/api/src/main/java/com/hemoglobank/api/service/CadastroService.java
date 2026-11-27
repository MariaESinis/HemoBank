package com.hemoglobank.api.service;

import com.hemoglobank.api.dto.DoadorRegistroDTO;
import com.hemoglobank.api.exception.RegraNegocioException;
import com.hemoglobank.api.model.Doador;
import com.hemoglobank.api.model.TipoUsuario;
import com.hemoglobank.api.model.Usuario;
import com.hemoglobank.api.repository.DoadorRepository;
import com.hemoglobank.api.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
public class CadastroService {

    public static final int IDADE_MINIMA = 16;
    public static final int IDADE_MAXIMA = 69;

    private final UsuarioRepository usuarioRepository;
    private final DoadorRepository doadorRepository;
    private final PasswordEncoder encoder;

    public CadastroService(UsuarioRepository usuarioRepository,
                           DoadorRepository doadorRepository,
                           PasswordEncoder encoder) {
        this.usuarioRepository = usuarioRepository;
        this.doadorRepository = doadorRepository;
        this.encoder = encoder;
    }

    /** Cria conta + doador na mesma transação: ou grava os dois ou nenhum. */
    @Transactional
    public Doador cadastrarDoador(DoadorRegistroDTO dto) {
        String email = dto.email().trim().toLowerCase();
        String nome = dto.nome().trim();

        int idade = Period.between(dto.dataNascimento(), LocalDate.now()).getYears();
        if (idade < IDADE_MINIMA || idade > IDADE_MAXIMA) {
            throw new RegraNegocioException(
                    "Para doar é preciso ter entre " + IDADE_MINIMA + " e " + IDADE_MAXIMA + " anos.");
        }
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new RegraNegocioException("Já existe uma conta com este e-mail.");
        }
        if (usuarioRepository.existsByCpf(dto.cpf()) || doadorRepository.existsByDorCpf(dto.cpf())) {
            throw new RegraNegocioException("Já existe um cadastro com este CPF.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setCpf(dto.cpf());
        usuario.setEmail(email);
        usuario.setTelefone(dto.telefone().trim());
        usuario.setSenha(encoder.encode(dto.senha()));
        usuario.setTipoUsuario(TipoUsuario.DOADOR);
        usuario = usuarioRepository.save(usuario);

        Doador doador = new Doador();
        doador.setDorNome(nome);
        doador.setDorCpf(dto.cpf());
        doador.setDorDataNascimento(dto.dataNascimento());
        doador.setDorSexo(dto.sexo());
        doador.setDorTelefone(dto.telefone().trim());
        doador.setDorEmail(email);
        doador.setUsuario(usuario);
        return doadorRepository.save(doador);
    }
}
