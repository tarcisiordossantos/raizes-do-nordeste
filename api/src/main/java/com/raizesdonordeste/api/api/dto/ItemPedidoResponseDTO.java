package com.raizesdonordeste.api.api.dto;

import java.math.BigDecimal;

import com.raizesdonordeste.api.domain.ItemPedido;

public record ItemPedidoResponseDTO(
    Long id,
    int quantidade,
    BigDecimal subtotal,
    String statusProduto,
    String produto //Retorna apenas o nome do produto
) {
    public static ItemPedidoResponseDTO fromEntity(ItemPedido entidade){
        return new ItemPedidoResponseDTO(
            entidade.getId(),
            entidade.getQuantidade(),
            entidade.getSubtotal(),
            entidade.getStatusProduto(),
            entidade.getProduto().getNome()
        );
    }
}
