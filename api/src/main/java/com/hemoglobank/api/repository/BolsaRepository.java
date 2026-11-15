package com.hemoglobank.api.repository;

import com.hemoglobank.api.model.Bolsa; // ou Bolsa, conforme o nome da sua classe
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BolsaRepository extends JpaRepository<Bolsa, Integer> {
    // Conta quantas bolsas existem de um determinado tipo sanguíneo com status DISPONIVEL
    long countByTipagemAndStatus(String tipagem, String status);
}