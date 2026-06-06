package com.raizesdonordeste.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
    Long id,
    LocalDateTime dataPedido,
    String statusPedido,
    String canalOrigem,
    String formaEntrega,
    BigDecimal valorEntrega,
    int prazoEstimado,
    BigDecimal desconto,
    BigDecimal valorTotal,
    String observacoes,
    Long usuarioId,
    Long unidadeId,
    List<ItemPedidoResponseDTO> itensPedido,
    List<PagamentoResponseDTO> pagamentos
) {
      
}
