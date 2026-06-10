package com.raizesdonordeste.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.raizesdonordeste.api.domain.Produto;
import java.util.List;



public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    boolean existsByNome(String nome);
    List<Produto> findByCategoria(String categoria);
    List<Produto> findByExigePreparo(boolean exigePreparo);
}
