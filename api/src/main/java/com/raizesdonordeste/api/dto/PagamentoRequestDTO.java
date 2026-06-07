package com.raizesdonordeste.api.dto;

import com.raizesdonordeste.api.domain.Pagamento;

public record PagamentoRequestDTO(
    String metodoPagamento
) {
    public Pagamento toEntity(){
        Pagamento p = new Pagamento();
        p.setMetodoPagamento(this.metodoPagamento());
        return p;
    }

}
