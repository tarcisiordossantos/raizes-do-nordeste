package com.raizesdonordeste.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.raizesdonordeste.api.domain.Pedido;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;



public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByDataPedidoBetween(LocalDateTime dataInicial, LocalDateTime dataFinal);
    List<Pedido> findByCanalOrigem(String canalOrigem);
    List<Pedido> findByUsuarioId(Long usuarioId);
    List<Pedido> findByUnidadeId(Long unidadeId);
    List<Pedido> findByUnidadeIdAndUsuarioId(Long unidadeId, Long usuarioId);
    Optional<Pedido> findTopByUsuarioIdOrderByIdDesc(Long usuarioId);
}
