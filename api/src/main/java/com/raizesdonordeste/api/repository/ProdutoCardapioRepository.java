package com.raizesdonordeste.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.raizesdonordeste.api.domain.ProdutoCardapio;


public interface ProdutoCardapioRepository extends JpaRepository<ProdutoCardapio, Long>{
    List<ProdutoCardapio> findByCardapioId(Long cardapioId);
}
