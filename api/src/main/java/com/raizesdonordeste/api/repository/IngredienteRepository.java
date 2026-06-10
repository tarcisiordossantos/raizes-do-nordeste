package com.raizesdonordeste.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.raizesdonordeste.api.domain.Ingrediente;


public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {
    boolean existsByNome(String nome);
}
