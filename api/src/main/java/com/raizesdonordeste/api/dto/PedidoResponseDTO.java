package com.raizesdonordeste.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.raizesdonordeste.api.domain.Pedido;

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
      public static PedidoResponseDTO fromEntity(Pedido entidade){
        List<ItemPedidoResponseDTO> itens = entidade.getItensPedido().stream()
        .map(item -> ItemPedidoResponseDTO.fromEntity(item)).toList();
    
        List<PagamentoResponseDTO> pagamentos = entidade.getPagamentos().stream()
        .map(p -> PagamentoResponseDTO.fromEntity(p)).toList();

        return new PedidoResponseDTO(
            entidade.getId(), 
            entidade.getDataPedido(), 
            entidade.getStatusPedido(), 
            entidade.getCanalOrigem(), 
            entidade.getFormaEntrega(), 
            entidade.getValorEntrega(), 
            entidade.getPrazoEstimado(), 
            entidade.getDesconto(), 
            entidade.getValorTotal(), 
            entidade.getObservacoes(), 
            entidade.getUsuario().getId(), 
            entidade.getUnidade().getId(), 
            itens, 
            pagamentos);
      }
}
