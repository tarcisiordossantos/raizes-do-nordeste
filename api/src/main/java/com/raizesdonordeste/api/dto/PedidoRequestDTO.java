package com.raizesdonordeste.api.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PedidoRequestDTO(
    @Size(max = 50) String canalOrigem,
    @Size(max = 50) String formaEntrega,
    @Size(max = 255) String observacoes,
    @NotNull Long usuarioId,
    @NotNull Long unidadeId,
    @Valid @NotEmpty List<ItemPedidoRequestDTO> itensPedido,
    @Valid @NotEmpty List<PagamentoRequestDTO> pagamentos
) {

}
