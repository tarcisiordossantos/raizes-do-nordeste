package com.raizesdonordeste.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raizesdonordeste.api.domain.Perfil;


@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    boolean existsByNome(String nome);
    Optional<Perfil> findByNome(String nome);
}
