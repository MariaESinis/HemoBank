package com.hemoglobank.api.repository;

import com.hemoglobank.api.model.Bolsa;
import com.hemoglobank.api.model.StatusBolsa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BolsaRepository extends JpaRepository<Bolsa, Integer> {

    /** Bolsas de uma tipagem, com o status informado e ainda dentro da validade. */
    long countByTipagemAndStatusAndValidadeGreaterThanEqual(String tipagem, StatusBolsa status, LocalDate validade);

    /** Bolsas disponíveis que vencem dentro de um intervalo (alerta para o gestor). */
    List<Bolsa> findByStatusAndValidadeBetweenOrderByValidadeAsc(StatusBolsa status, LocalDate inicio, LocalDate fim);

    List<Bolsa> findTop10ByOrderByIdDesc();
}
