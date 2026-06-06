package com.raizesdonordeste.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemPedidoRequestDTO(
    @Min(value = 1) int quantidade,
    @NotNull Long produtoId
) {

}
