package com.raizesdonordeste.api.dto;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
    Long id,
    int quantidade,
    BigDecimal subtotal,
    String statusProduto,
    String produto //Retorna apenas o nome do produto
) {

}
