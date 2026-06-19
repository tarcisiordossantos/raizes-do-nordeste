package com.raizesdonordeste.api.api.dto;

import com.raizesdonordeste.api.domain.Endereco;

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
    public static EnderecoResponseDTO fromEntity(Endereco entidade){
        return new EnderecoResponseDTO(
            entidade.getId(),
            entidade.getCep(),
            entidade.getLogradouro(),
            entidade.getNumero(),
            entidade.getComplemento(),
            entidade.getBairro(),
            entidade.getCidade(),
            entidade.getEstado(),
            entidade.isPrincipal()
        );
    }
}
