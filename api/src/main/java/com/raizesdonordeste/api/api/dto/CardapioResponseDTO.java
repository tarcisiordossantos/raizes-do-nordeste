package com.raizesdonordeste.api.api.dto;

import java.util.List;

public record CardapioResponseDTO(
    Long id,
    String nome,
    String descricao,
    boolean ativo,
    Long unidadeId,
    List<ProdutoCardapioResponseDTO> produtosCardapio
) {

}
