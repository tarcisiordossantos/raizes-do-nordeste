package com.raizesdonordeste.api.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.raizesdonordeste.api.domain.IngredienteProduto;


public interface IngredienteProdutoRepository extends JpaRepository<IngredienteProduto, Long> {

}
