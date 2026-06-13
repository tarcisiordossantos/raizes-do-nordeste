package com.raizesdonordeste.api.dto;

import com.raizesdonordeste.api.domain.ItemPedido;
import com.raizesdonordeste.api.domain.Produto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemPedidoRequestDTO(
    @NotNull(message = "campo de preenchimento obrigatório") 
    @Min(value = 1) 
    int quantidade,
    @NotNull(message = "campo de preenchimento obrigatório") 
    Long produtoId
) {
    public ItemPedido toEntity(Produto produto){
            ItemPedido item = new ItemPedido();
            item.setQuantidade(this.quantidade);
            item.setProduto(produto);
            item.setSubtotal(item.calcularSubtotal());
            return item;
    }
}

