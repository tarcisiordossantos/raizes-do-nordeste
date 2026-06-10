package com.raizesdonordeste.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.raizesdonordeste.api.domain.Perfil;



public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    boolean existsByNome(String nome);
    Optional<Perfil> findByNome(String nome);
}
