package com.hemoglobank.api.repository;

import com.hemoglobank.api.model.Doador;
import com.hemoglobank.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoadorRepository extends JpaRepository<Doador, Integer> {
    // Busca o doador associado à entidade Usuario
    Optional<Doador> findByUsuario(Usuario usuario);
}