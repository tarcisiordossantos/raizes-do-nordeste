package com.raizesdonordeste.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.raizesdonordeste.api.domain.Usuario;



public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    List<Usuario> findByPerfisNome(String nomePerfil);
    Optional<Usuario> findByEmail(String email);
}
