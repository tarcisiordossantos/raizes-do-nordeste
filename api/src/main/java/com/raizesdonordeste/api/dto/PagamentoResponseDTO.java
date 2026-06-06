package com.raizesdonordeste.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoResponseDTO(
    Long id,
    String metodoPagamento,
    String statusPagamento,
    BigDecimal valor,
    LocalDateTime dataPagamento
) {

}
