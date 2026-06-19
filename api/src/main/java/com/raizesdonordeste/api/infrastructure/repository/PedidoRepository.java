package com.raizesdonordeste.api.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import com.raizesdonordeste.api.domain.Pedido;
import com.raizesdonordeste.api.domain.enuns.CanalOrigem;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;



public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByDataPedidoBetween(LocalDateTime dataInicial, LocalDateTime dataFinal);
    Page<Pedido> findByUsuarioId(Long usuarioId, Pageable pageable);
    Page<Pedido> findByUnidadeId(Long unidadeId, Pageable pageable);
    Page<Pedido> findByUnidadeIdAndUsuarioId(Long unidadeId, Long usuarioId,Pageable pageable);
    Page<Pedido> findByCanalOrigem(CanalOrigem canalOrigem, Pageable pageable);
    Page<Pedido> findByUnidadeIdAndCanalOrigem(Long unidadeId, CanalOrigem canalOrigem, Pageable pageable);
    Optional<Pedido> findTopByUsuarioIdOrderByIdDesc(Long usuarioId);
}
