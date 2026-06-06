package com.raizesdonordeste.api.dto;

public record EnderecoResponseDTO(
    Long id,
    String cep,
    String logradouro,
    String numero,
    String complemento,
    String bairro,
    String cidade,
    String estado,
    boolean principal
) {

}
