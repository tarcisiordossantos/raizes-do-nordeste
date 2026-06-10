package com.raizesdonordeste.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.raizesdonordeste.api.domain.Endereco;


public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    Optional<Endereco> findByUsuarioIdAndPrincipalTrue(Long usuarioId);
}
