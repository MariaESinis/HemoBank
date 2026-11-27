package com.hemoglobank.api.repository;

import com.hemoglobank.api.model.Agendamento;
import com.hemoglobank.api.model.Doador;
import com.hemoglobank.api.model.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {

    List<Agendamento> findByDoadorOrderByAgeDataDescAgeHorarioDesc(Doador doador);

    List<Agendamento> findAllByOrderByAgeDataAscAgeHorarioAsc();

    boolean existsByDoadorAndAgeStatusIn(Doador doador, Collection<StatusAgendamento> status);

    long countByAgeStatus(StatusAgendamento status);
}
