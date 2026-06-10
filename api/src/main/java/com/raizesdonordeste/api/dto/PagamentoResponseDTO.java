package com.raizesdonordeste.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.raizesdonordeste.api.domain.Pagamento;

public record PagamentoResponseDTO(
    Long id,
    String metodoPagamento,
    String statusPagamento,
    BigDecimal valor,
    LocalDateTime dataPagamento
) {
    public static PagamentoResponseDTO fromEntity(Pagamento p){
        return new PagamentoResponseDTO(
            p.getId(), 
            p.getMetodoPagamento(), 
            p.getStatusPagamento(), 
            p.getValor(), 
            p.getDataPagamento());
    }
}
