package com.raizesdonordeste.api.dto;

public record ProdutoCardapioResponseDTO(
    Long id,
    boolean disponivel,
    String produto //Apenas nome do produto
) {

}
