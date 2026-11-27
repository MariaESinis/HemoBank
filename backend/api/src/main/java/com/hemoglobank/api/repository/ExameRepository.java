package com.hemoglobank.api.repository;

import com.hemoglobank.api.model.Doador;
import com.hemoglobank.api.model.Exame;
import com.hemoglobank.api.model.StatusExame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ExameRepository extends JpaRepository<Exame, Integer> {

    List<Exame> findByDoadorOrderByIdDesc(Doador doador);

    List<Exame> findAllByOrderByIdDesc();

    boolean existsByDoadorAndTipoAndStatusIn(Doador doador, String tipo, Collection<StatusExame> status);

    long countByStatusIn(Collection<StatusExame> status);
}
