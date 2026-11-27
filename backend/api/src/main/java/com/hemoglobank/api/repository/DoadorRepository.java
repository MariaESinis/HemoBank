package com.hemoglobank.api.repository;

import com.hemoglobank.api.model.Doador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoadorRepository extends JpaRepository<Doador, Integer> {

    Optional<Doador> findByDorCpf(String dorCpf);

    boolean existsByDorCpf(String dorCpf);

    /** Doador associado ao e-mail de login (usuario.email). */
    Optional<Doador> findByUsuarioEmailIgnoreCase(String email);
}
