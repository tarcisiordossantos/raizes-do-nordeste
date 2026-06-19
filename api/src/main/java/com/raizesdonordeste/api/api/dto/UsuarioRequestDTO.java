package com.raizesdonordeste.api.api.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import com.raizesdonordeste.api.domain.Endereco;
import com.raizesdonordeste.api.domain.Usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
    @Schema(example = "123.456.789-10")
    @NotBlank(message = "campo de preenchimento obrigatório")
    //@CPF(message = "Deve utilizar o formato 123.456.789-10 ou 12345678910")
    @Size(min = 11, max = 14, message = "Campo deve ter no mínimo 11 e no máximo 14 caracteres") 
    String cpf,
    @Schema(example = "FULANO DE TAL")
    @NotBlank(message = "campo de preenchimento obrigatório") 
    @Size(max = 150) 
    String nome,
    @NotNull(message = "campo de preenchimento obrigatório") 
    @Schema(example = "2000-01-31")
    @Past(message = "data de nascimento deve estar no passado") 
    LocalDate dataNascimento,
    @Schema(example = "exemplo@email.com")
    @NotBlank(message = "campo de preenchimento obrigatório") 
    @Email (message = "campo deve ser preenchido em formato de e-mail válido: exemplo@mail.com")
    @Size(max = 150) 
    String email,
    @Schema(example = "(11)98888-8888 ou (11)8888-8888")
    @Pattern(regexp = "^(?:\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4})?$", message = "deve ser preenchido no formato (11)98888-8888 ou (11)8888-8888")
    String telefone,
    @Schema(example = "******")
    @NotBlank(message = "campo de preenchimento obrigatório") 
    @Size(min = 6, max = 255, message = "Campo deve ter no mínimo 6 e no máximo 255 caracteres") 
    String senha,
    @Schema(example = "M ou F")
    @Size(max = 1) 
    @Pattern(regexp = "^(M|F)?$", message = "o campo apenas aceita M ou F")
    String genero,
    @Valid EnderecoRequestDTO endereco,
    @NotNull(message = "É necessário aceitar os Termos de Uso e Privacidade") 
    @AssertTrue(message = "É necessário aceitar os Termos de Uso e Privacidade")
    Boolean termoPrivacidade
) {
    public Usuario toEntity(){
        Usuario usuario = new Usuario();
        usuario.setCpf(this.cpf.replaceAll("\\D", ""));
        usuario.setNome(this.nome);
        usuario.setDataNascimento(this.dataNascimento);
        usuario.setEmail(this.email.toLowerCase());
        if(this.telefone != null){
            usuario.setTelefone(this.telefone.replaceAll("\\D", ""));
        }
        usuario.setSenha(this.senha);
        usuario.setGenero(this.genero);
        usuario.setPontosFidelidade(0);
  
        if(this.endereco != null){
            Endereco enderecoEntity = this.endereco.toEntity();
            enderecoEntity.setUsuario(usuario);
            usuario.getEnderecos().add(enderecoEntity);
        }

        return usuario;
    }
}
