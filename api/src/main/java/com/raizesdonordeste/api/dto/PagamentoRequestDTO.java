package com.raizesdonordeste.api.dto;

import java.time.YearMonth;

import com.raizesdonordeste.api.domain.Pagamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PagamentoRequestDTO(
    @Schema(example = "PIX|DEBITO|CREDITO")
    @NotBlank(message = "campo de preenchimento obrigatório") 
    @Pattern(regexp = "^(PIX|DEBITO|CREDITO)$", message = "Deve preencher uma das três opções: PIX, DEBITO ou CREDITO") 
    String metodoPagamento,
    @Schema(example = "1234123412341234")
    @Pattern(regexp = "^(?:\\d{16})?$", message = "Deve preencher os 16 números do cartão para método de pagamento DEBITO ou CREDITO")
    String numeroCartao,
    @Schema(example = "2030-12")
    @FutureOrPresent(message = "O cartão não pode estar vencido para método de pagamento DEBITO ou CREDITO")
    YearMonth vencimentoCartao,
    @Schema(example = "000")
    @Pattern(regexp = "^(?:\\d{3,4})?$", message = "Deve preencher o código de segurança para método de pagamento DEBITO ou CREDITO")
    String cvv

) {
    public Pagamento toEntity(){
        Pagamento p = new Pagamento();
        p.setMetodoPagamento(this.metodoPagamento());
        return p;
    }

}
