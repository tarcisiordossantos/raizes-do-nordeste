package com.raizesdonordeste.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.raizesdonordeste.api.domain.EstoqueProduto;

@Repository
public interface EstoqueProdutoRepository extends JpaRepository<EstoqueProduto, Long>{
    @Query("SELECT e FROM EstoqueProduto e WHERE e.unidade.id = :unidadeId AND e.quantidadeAtual < e.quantidadeMinima")
    List<EstoqueProduto> findAbaixoDoMinimoPorUnidade(Long unidadeId);

    List<EstoqueProduto> findByUnidadeId(Long unidadeId);
}
