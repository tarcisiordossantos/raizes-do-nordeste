package com.raizesdonordeste.api.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.raizesdonordeste.api.domain.Cardapio;



public interface CardapioRepository extends JpaRepository<Cardapio, Long>{
    Optional<Cardapio> findByUnidadeIdAndNome(Long unidadeId, String nomeCardapio);
    Optional<Cardapio> findByUnidadeIdAndAtivoTrue(Long unidadeId);
}
