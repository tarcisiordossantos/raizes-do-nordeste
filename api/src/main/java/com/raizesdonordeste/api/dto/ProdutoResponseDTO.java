package com.raizesdonordeste.api.dto;

import java.math.BigDecimal;

import com.raizesdonordeste.api.domain.Produto;

public record ProdutoResponseDTO(
    Long id,
    String nome,
    String descricao,
    BigDecimal preco
) {
    public static ProdutoResponseDTO fromEntity(Produto produto){
        return new ProdutoResponseDTO(
            produto.getId(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPrecoBase()
        );

    }

}
