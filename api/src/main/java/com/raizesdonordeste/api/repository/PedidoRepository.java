package com.raizesdonordeste.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raizesdonordeste.api.domain.Pedido;
import java.util.List;
import java.time.LocalDateTime;


@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByDataPedidoBetween(LocalDateTime dataInicial, LocalDateTime dataFinal);
    List<Pedido> findByCanalOrigem(String canalOrigem);
    List<Pedido> findByUsuarioId(Long usuarioId);
    List<Pedido> findByUnidadeId(Long unidadeId);
    List<Pedido> findByUnidadeIdAndStatusPedido(Long unidadeId, String statusPedido);
}
