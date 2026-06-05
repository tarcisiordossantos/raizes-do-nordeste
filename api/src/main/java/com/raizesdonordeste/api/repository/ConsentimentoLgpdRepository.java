package com.raizesdonordeste.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raizesdonordeste.api.domain.ConsentimentoLgpd;

@Repository
public interface ConsentimentoLgpdRepository extends JpaRepository<ConsentimentoLgpd, Long> {
    
}
