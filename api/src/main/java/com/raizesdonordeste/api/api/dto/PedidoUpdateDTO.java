package com.raizesdonordeste.api.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PedidoUpdateDTO(
    @Schema(example = "CANCELADO")
    @Pattern(regexp = "^(CANCELADO)?$", message = "O campo deve ser preenchido com CANCELADO ou deixado em branco")
    String statusPedido,
    @Size(max = 255)
    String observacoes,
    @Schema(example = "1")
    @NotNull(message = "campo de preenchimento obrigatório") 
    Long usuarioId
) {

}
