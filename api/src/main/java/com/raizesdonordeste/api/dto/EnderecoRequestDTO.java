package com.raizesdonordeste.api.dto;

import com.raizesdonordeste.api.domain.Endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnderecoRequestDTO(
    @NotBlank @Size(max = 10) String cep,
    @NotBlank @Size(max = 150) String logradouro,
    @NotBlank @Size(max = 10) String numero,
    @Size(max = 100) String complemento,
    @NotBlank @Size(max = 100) String bairro,
    @NotBlank @Size(max = 100) String cidade,
    @NotBlank @Size(max = 2, message = "Deve ser informado apenas a sigla do estado com dois caracteres")
    String estado
) {
    public Endereco toEntity(){
        Endereco endereco = new Endereco();
        endereco.setCep(this.cep);
        endereco.setLogradouro(this.logradouro);
        endereco.setNumero(this.numero);
        endereco.setComplemento(this.complemento);
        endereco.setBairro(this.bairro);
        endereco.setCidade(this.cidade);
        endereco.setEstado(this.estado);
        return endereco;
    }

}
