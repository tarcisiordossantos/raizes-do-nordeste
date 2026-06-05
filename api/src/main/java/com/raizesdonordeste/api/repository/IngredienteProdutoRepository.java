package com.raizesdonordeste.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raizesdonordeste.api.domain.IngredienteProduto;

@Repository
public interface IngredienteProdutoRepository extends JpaRepository<IngredienteProduto, Long> {

}
