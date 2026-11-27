package com.hemoglobank.api.service;

import com.hemoglobank.api.exception.RegraNegocioException;
import com.hemoglobank.api.model.Bolsa;
import com.hemoglobank.api.model.ComponenteSanguineo;
import com.hemoglobank.api.model.Doador;
import com.hemoglobank.api.model.StatusBolsa;
import com.hemoglobank.api.repository.BolsaRepository;
import com.hemoglobank.api.repository.DoadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class BolsaService {

    private static final int QUANTIDADE_MAX_ML = 1000;

    private final BolsaRepository bolsaRepository;
    private final DoadorRepository doadorRepository;

    public BolsaService(BolsaRepository bolsaRepository, DoadorRepository doadorRepository) {
        this.bolsaRepository = bolsaRepository;
        this.doadorRepository = doadorRepository;
    }

    /** Cadastra a bolsa e registra a doação (data da última doação) do doador. */
    @Transactional
    public Bolsa cadastrar(String cpfDoador, String tipagem, ComponenteSanguineo componente,
                           Integer quantidade, LocalDate validade) {
        String cpf = cpfDoador == null ? "" : cpfDoador.replaceAll("\\D", "");
        Doador doador = doadorRepository.findByDorCpf(cpf)
                .orElseThrow(() -> new RegraNegocioException("Nenhum doador cadastrado com o CPF informado."));

        if (!EstoqueService.TIPAGENS.contains(tipagem)) {
            throw new RegraNegocioException("Tipagem sanguínea inválida.");
        }
        if (componente == null) {
            throw new RegraNegocioException("Informe o componente.");
        }
        if (quantidade == null || quantidade <= 0 || quantidade > QUANTIDADE_MAX_ML) {
            throw new RegraNegocioException("A quantidade deve estar entre 1 e " + QUANTIDADE_MAX_ML + " ml.");
        }
        if (validade == null || validade.isBefore(LocalDate.now())) {
            throw new RegraNegocioException("A validade deve ser hoje ou uma data futura.");
        }

        Bolsa bolsa = new Bolsa();
        bolsa.setDoador(doador);
        bolsa.setTipagem(tipagem);
        bolsa.setComponente(componente);
        bolsa.setQuantidade(quantidade);
        bolsa.setValidade(validade);
        bolsa.setStatus(StatusBolsa.DISPONIVEL);

        doador.registrarDoacaoEm(LocalDate.now());
        doadorRepository.save(doador);
        return bolsaRepository.save(bolsa);
    }
}
