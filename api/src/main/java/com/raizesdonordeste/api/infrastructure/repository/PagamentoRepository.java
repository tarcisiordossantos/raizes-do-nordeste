package com.raizesdonordeste.api.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.raizesdonordeste.api.domain.Pagamento;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;



public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    Optional<Pagamento> findByPedidoIdAndStatusPagamento(Long pedidoId, String statusPagamento);
    List<Pagamento> findByMetodoPagamento(String metodoPagamento);
    List<Pagamento> findByStatusPagamento(String statusPagamento);
    List<Pagamento> findByDataPagamentoBetween(LocalDateTime dataInicial, LocalDateTime dataFinal);
    List<Pagamento> findByStatusPagamentoAndDataPagamentoBetween(String statusPagamento, LocalDateTime dataInicial, LocalDateTime dataFinal);
}
