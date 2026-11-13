package com.hemoglobank.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hemoglobank.api.model.Bolsa;

@Repository 
public interface BolsaRepository extends JpaRepository<Bolsa,Long>{
    
}
