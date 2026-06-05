package com.raizesdonordeste.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raizesdonordeste.api.domain.RegiaoComercial;

@Repository
public interface RegiaoComercialRepository extends JpaRepository<RegiaoComercial, Long> {
    boolean existsByNome(String nome);
}
