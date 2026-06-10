package com.raizesdonordeste.api.dto;

import com.raizesdonordeste.api.domain.Pagamento;

import jakarta.validation.constraints.NotBlank;

public record PagamentoRequestDTO(
    @NotBlank String metodoPagamento
) {
    public Pagamento toEntity(){
        Pagamento p = new Pagamento();
        p.setMetodoPagamento(this.metodoPagamento());
        return p;
    }

}
