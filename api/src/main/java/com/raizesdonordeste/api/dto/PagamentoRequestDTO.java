package com.raizesdonordeste.api.dto;

import com.raizesdonordeste.api.domain.Pagamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PagamentoRequestDTO(
    @Schema(example = "PIX|DEBITO|CREDITO")
    @NotBlank(message = "campo de preenchimento obrigatório") 
    @Pattern(regexp = "^(PIX|DEBITO|CREDITO)$", message = "Deve preencher uma das três opções: PIX, DEBITO ou CREDITO") 
    String metodoPagamento
) {
    public Pagamento toEntity(){
        Pagamento p = new Pagamento();
        p.setMetodoPagamento(this.metodoPagamento());
        return p;
    }

}
