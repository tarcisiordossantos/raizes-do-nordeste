package com.raizesdonordeste.api.dto;

import java.math.BigDecimal;

public record ProdutoResponseDTO(
    Long id,
    String nome,
    String descricao,
    String categoria,
    BigDecimal precoBase
) {

}
