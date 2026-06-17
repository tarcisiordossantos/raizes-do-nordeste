package com.raizesdonordeste.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
    @Schema(example = "123.456.789-10")
    @NotBlank(message = "campo de preenchimento obrigatório")
    //@CPF(message = "Deve utilizar o formato 123.456.789-10 ou 12345678910")
    @Size(min = 11, max = 14, message = "Campo deve ter no mínimo 11 e no máximo 14 caracteres") 
    String cpf, 
    @Schema(example = "******")
    @NotBlank(message = "campo de preenchimento obrigatório") 
    @Size(min = 6, max = 255, message = "Campo deve ter no mínimo 6 e no máximo 255 caracteres") 
    String senha) {

}
