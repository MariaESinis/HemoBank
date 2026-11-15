package com.hemoglobank.api.repository;

import com.hemoglobank.api.model.Agendamento;
import com.hemoglobank.api.model.Doador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {
    // Busca a lista de agendamentos de um doador específico
    List<Agendamento> findByDoador(Doador doador);
}