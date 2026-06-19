package com.raizesdonordeste.api.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.raizesdonordeste.api.domain.RegiaoComercial;


public interface RegiaoComercialRepository extends JpaRepository<RegiaoComercial, Long> {
    boolean existsByNome(String nome);
}
