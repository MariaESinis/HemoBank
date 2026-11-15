package com.hemoglobank.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hemoglobank.api.model.Estoque;

@Repository 
public interface EstoqueRepository extends JpaRepository<Estoque,Long>{
    
}
