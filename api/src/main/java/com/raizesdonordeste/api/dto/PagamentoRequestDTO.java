package com.raizesdonordeste.api.dto;

import java.time.LocalDateTime;

public record PagamentoRequestDTO(
    String metodoPagamento,
    LocalDateTime dataPagamento
) {
    

}
