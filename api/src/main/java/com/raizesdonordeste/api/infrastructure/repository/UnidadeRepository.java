package com.raizesdonordeste.api.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.raizesdonordeste.api.domain.Unidade;


public interface UnidadeRepository extends JpaRepository<Unidade, Long>{
    boolean existsByCnpj(String cnpj);
    Optional<Unidade> findByCnpj(String cnpj);
    List<Unidade> findByEnderecoCidade(String cidade);
    List<Unidade> findByEnderecoEstado(String estado);
    List<Unidade> findByRegiaoId(Long regiaoId);
}
