package com.raizesdonordeste.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raizesdonordeste.api.domain.Unidade;

@Repository
public interface UnidadeRepository extends JpaRepository<Unidade, Long>{
    boolean existsByCnpj(String cnpj);
    Optional<Unidade> findByCnpj(String cnpj);
    List<Unidade> findByEnderecoCidade(String cidade);
    List<Unidade> findByEnderecoEstado(String estado);
    List<Unidade> findByRegiaoId(Long regiaoId);
}
