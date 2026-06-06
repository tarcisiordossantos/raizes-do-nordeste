package com.raizesdonordeste.api.dto;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
    @NotBlank @Size(min = 11, max = 14) String cpf,
    @NotBlank @Size(max = 150)String nome,
    @NotBlank LocalDate dataNascimento,
    @NotBlank @Email @Size(max = 150) String email,
    @Size(max = 20) String telefone,
    @NotBlank @Size(min = 6, max = 255) String senha,
    @Size(max = 30) String genero,
    @Valid EnderecoRequestDTO endereco
) {

}
