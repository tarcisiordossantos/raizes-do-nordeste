package com.raizesdonordeste.api.gateway;

import java.time.YearMonth;

import org.springframework.stereotype.Component;

import com.raizesdonordeste.api.domain.Pedido;

@Component
public class PagamentoGateway {
    public boolean validarPagamentoPix(Pedido pedido){
        Long dividendo = pedido.getUsuario().getId();
        return dividendo % 2L != 0;
    }
    public boolean validarPagamentoCartao(Pedido pedido, String numero, YearMonth vencimento, String cvv){
        Long dividendo = pedido.getUsuario().getId();
        return dividendo % 2L != 0;
    }
}
