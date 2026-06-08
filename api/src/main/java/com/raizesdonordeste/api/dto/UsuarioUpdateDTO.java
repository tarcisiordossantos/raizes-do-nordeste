package com.raizesdonordeste.api.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateDTO(
    @Size(max = 150) String nome,
    @Past LocalDate dataNascimento,
    @Email @Size(max = 150) String email,
    @Size(max = 20) String telefone,
    @Size(min = 6, max = 255) String senha,
    @Size(max = 30) String genero
) {

}
