package com.raizesdonordeste.api.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.raizesdonordeste.api.domain.ConsentimentoLgpd;


public interface ConsentimentoLgpdRepository extends JpaRepository<ConsentimentoLgpd, Long> {
    List<ConsentimentoLgpd> findByUsuarioIdAndAceitoTrue(Long usuarioId);
    List<ConsentimentoLgpd> findByUsuarioIdAndTipoConsentimento(Long usuarioId, String tipoConsentimento);
}
