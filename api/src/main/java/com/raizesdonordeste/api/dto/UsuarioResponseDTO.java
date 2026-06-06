package com.raizesdonordeste.api.dto;

import java.time.LocalDate;
import java.util.List;

public record UsuarioResponseDTO(
    Long id,
    String cpf,
    String nome,
    LocalDate dataNascimento,
    String email,
    String telefone,
    String genero,
    int pontosFidelidade,
    List<String> perfis,
    List<EnderecoResponseDTO> enderecos
) {

}
