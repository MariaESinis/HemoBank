package com.hemoglobank.api.service;

import com.hemoglobank.api.exception.RegraNegocioException;
import com.hemoglobank.api.model.Agendamento;
import com.hemoglobank.api.model.Doador;
import com.hemoglobank.api.model.StatusAgendamento;
import com.hemoglobank.api.repository.AgendamentoRepository;
import com.hemoglobank.api.repository.DoadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class AgendamentoService {

    private static final DateTimeFormatter DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final int OBSERVACOES_MAX = 500;

    private static final List<StatusAgendamento> EM_ABERTO = Arrays.stream(StatusAgendamento.values())
            .filter(StatusAgendamento::isEmAberto)
            .toList();

    private final AgendamentoRepository agendamentoRepository;
    private final DoadorRepository doadorRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, DoadorRepository doadorRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.doadorRepository = doadorRepository;
    }

    @Transactional
    public Agendamento agendar(Doador doador, LocalDate data, LocalTime horario, String observacoes) {
        if (data == null || horario == null) {
            throw new RegraNegocioException("Informe a data e o horário da coleta.");
        }
        if (LocalDateTime.of(data, horario).isBefore(LocalDateTime.now())) {
            throw new RegraNegocioException("Escolha uma data e um horário futuros.");
        }
        LocalDate liberadaEm = doador.calcularProximaDoacao();
        if (data.isBefore(liberadaEm)) {
            throw new RegraNegocioException(
                    "Você poderá doar novamente a partir de " + liberadaEm.format(DATA_BR) + ".");
        }
        if (agendamentoRepository.existsByDoadorAndAgeStatusIn(doador, EM_ABERTO)) {
            throw new RegraNegocioException(
                    "Você já tem um agendamento em aberto. Cancele-o antes de marcar outro.");
        }
        String obs = limpar(observacoes);
        if (obs != null && obs.length() > OBSERVACOES_MAX) {
            throw new RegraNegocioException("As observações devem ter no máximo " + OBSERVACOES_MAX + " caracteres.");
        }

        Agendamento agendamento = new Agendamento();
        agendamento.setDoador(doador);
        agendamento.setAgeData(data);
        agendamento.setAgeHorario(horario);
        agendamento.setAgeObservacoes(obs);
        agendamento.setAgeStatus(StatusAgendamento.PENDENTE);
        return agendamentoRepository.save(agendamento);
    }

    /** O doador só pode cancelar os próprios agendamentos que ainda estão em aberto. */
    @Transactional
    public void cancelar(Integer id, Doador doador) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .filter(a -> a.getDoador() != null
                        && Objects.equals(a.getDoador().getDorId(), doador.getDorId()))
                .orElseThrow(() -> new RegraNegocioException("Agendamento não encontrado."));

        if (!agendamento.getAgeStatus().isEmAberto()) {
            throw new RegraNegocioException("Só é possível cancelar agendamentos em aberto.");
        }
        agendamento.setAgeStatus(StatusAgendamento.CANCELADO);
        agendamentoRepository.save(agendamento);
    }

    /** Atualização feita pelo médico. Ao concluir, registra a doação para o cálculo da próxima. */
    @Transactional
    public void atualizar(Integer id, StatusAgendamento status, String observacoes) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Agendamento não encontrado."));

        String obs = limpar(observacoes);
        if (obs != null && obs.length() > OBSERVACOES_MAX) {
            throw new RegraNegocioException("As observações devem ter no máximo " + OBSERVACOES_MAX + " caracteres.");
        }

        agendamento.setAgeStatus(status);
        agendamento.setAgeObservacoes(obs);
        agendamentoRepository.save(agendamento);

        if (status == StatusAgendamento.CONCLUIDO && agendamento.getDoador() != null) {
            Doador doador = agendamento.getDoador();
            doador.registrarDoacaoEm(agendamento.getAgeData());
            doadorRepository.save(doador);
        }
    }

    private static String limpar(String texto) {
        if (texto == null) return null;
        String t = texto.trim();
        return t.isEmpty() ? null : t;
    }
}
