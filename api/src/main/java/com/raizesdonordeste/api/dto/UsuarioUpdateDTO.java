package com.raizesdonordeste.api.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateDTO(
    @Size(max = 150) 
    String nome,
    @Schema(example = "2000-01-31")
    @Past(message = "data de nascimento deve estar no passado")  
    LocalDate dataNascimento,
    @Schema(example = "exemplo@mail.com")
    @Email @Size(max = 150) 
    String email,
    @Schema(example = "(11)98888-8888 ou (11)8888-8888")
    @Pattern(regexp = "^(?:\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4})?$", message = "deve ser preenchido no formato (11)98888-8888 ou (11)8888-8888")
    String telefone,
    @Size(min = 6, max = 255, message = "Campo deve ter no mínimo 6 e no máximo 255 caracteres") 
    String senha,
    @Schema(example = "M ou F")
    @Size(max = 1) 
    @Pattern(regexp = "^(M|F)?$", message = "o campo apenas aceita M ou F")
    String genero
) {

}
