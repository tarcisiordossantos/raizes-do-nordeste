package com.raizesdonordeste.api.api.dto;

import com.raizesdonordeste.api.domain.Endereco;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoRequestDTO(
    @Schema(example = "45000-000 ou 45000000")
    @NotBlank(message = "campo de preenchimento obrigatório")
    @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "Deve utilizar o formato 45000-000 ou 45000000")  
    String cep,
    @Schema(example = "Rua Principal")
    @NotBlank(message = "campo de preenchimento obrigatório") 
    @Size(max = 150) 
    String logradouro,
    @Schema(example = "100")
    @NotBlank(message = "campo de preenchimento obrigatório") 
    @Pattern(regexp = "^[0-9]+$", message = "campo permite apenas números")
    @Size(max = 5) 
    String numero,
    @Schema(example = "casa")
    @Size(max = 100) 
    String complemento,
    @Schema(example = "Centro")
    @NotBlank (message = "campo de preenchimento obrigatório")
    @Size(max = 100) 
    String bairro,
    @Schema(example = "Itabuna")
    @NotBlank(message = "campo de preenchimento obrigatório") 
    @Size(max = 100) 
    String cidade,
    @Schema(example = "BA")
    @Pattern(regexp = "^(AC|AL|AP|AM|BA|CE|DF|ES|GO|MA|MT|MS|MG|PA|PB|PR|PE|PI|RJ|RN|RS|RO|RR|SC|SP|SE|TO)?$", message = "Deve ser informado apenas a sigla do estado com dois caracteres")
    @NotBlank(message = "campo de preenchimento obrigatório") 
    @Size(max = 2, message = "Deve ser informado apenas a sigla do estado com dois caracteres")
    String estado,
    Boolean principal
) {
    public Endereco toEntity(){
        Endereco endereco = new Endereco();
        endereco.setCep(this.cep.replaceAll("\\D", ""));
        endereco.setLogradouro(this.logradouro);
        endereco.setNumero(this.numero);
        endereco.setComplemento(this.complemento);
        endereco.setBairro(this.bairro);
        endereco.setCidade(this.cidade);
        endereco.setEstado(this.estado);
        endereco.setPrincipal(principal);
        return endereco;
    }

}
