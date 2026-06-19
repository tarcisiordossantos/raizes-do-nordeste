package com.raizesdonordeste.api.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.raizesdonordeste.api.domain.EstoqueIngrediente;


public interface EstoqueIngredienteRepository extends JpaRepository<EstoqueIngrediente, Long> {
    
    @Query("SELECT e FROM EstoqueIngrediente e WHERE e.unidade.id = :unidadeId AND e.quantidadeAtual < e.quantidadeMinima")
    List<EstoqueIngrediente> findAbaixoDoMinimoPorUnidade(Long unidadeId);

    List<EstoqueIngrediente> findByUnidadeId(Long unidadeId);
}
