package com.raizesdonordeste.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.raizesdonordeste.api.domain.EstoqueIngrediente;

@Repository
public interface EstoqueIngredienteRepository extends JpaRepository<EstoqueIngrediente, Long> {
    
    @Query("SELECT e FROM EstoqueIngrediente e WHERE e.unidade.id = :unidadeId AND e.quantidadeAtual < e.quantidadeMinima")
    List<EstoqueIngrediente> findAbaixoDoMinimoPorUnidade(Long unidadeId);

    List<EstoqueIngrediente> findByUnidadeId(Long unidadeId);
}
