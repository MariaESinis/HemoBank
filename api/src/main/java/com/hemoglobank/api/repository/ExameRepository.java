package com.hemoglobank.api.repository;

import com.hemoglobank.api.model.Doador;
import com.hemoglobank.api.model.Exame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExameRepository extends JpaRepository<Exame, Integer> {
    // Busca a lista de exames vinculados a um doador específico
    List<Exame> findByDoador(Doador doador);
}