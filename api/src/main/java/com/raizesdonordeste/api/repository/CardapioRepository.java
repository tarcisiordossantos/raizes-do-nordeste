package com.raizesdonordeste.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raizesdonordeste.api.domain.Cardapio;


@Repository
public interface CardapioRepository extends JpaRepository<Cardapio, Long>{
    Optional<Cardapio> findByUnidadeIdAndNome(Long unidadeId, String nomeCardapio);
    Optional<Cardapio> findByUnidadeIdAndAtivoTrue(Long unidadeId);
}
