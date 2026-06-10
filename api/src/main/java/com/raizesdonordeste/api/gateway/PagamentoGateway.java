package com.raizesdonordeste.api.gateway;

import org.springframework.stereotype.Component;

import com.raizesdonordeste.api.domain.Pedido;

@Component
public class PagamentoGateway {
    public boolean validarPagamento(Pedido pedido){
        Long dividendo = pedido.getUsuario().getId();
        return dividendo % 2L == 0;
    }
}
