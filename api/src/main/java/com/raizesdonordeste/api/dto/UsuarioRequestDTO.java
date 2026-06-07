package com.raizesdonordeste.api.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import com.raizesdonordeste.api.domain.Endereco;
import com.raizesdonordeste.api.domain.Usuario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
    @NotBlank @CPF @Size(min = 11, max = 14) String cpf,
    @NotBlank @Size(max = 150) String nome,
    @NotNull @Past LocalDate dataNascimento,
    @NotBlank @Email @Size(max = 150) String email,
    @Size(max = 20) String telefone,
    @NotBlank @Size(min = 6, max = 255) String senha,
    @Size(max = 30) String genero,
    @Valid EnderecoRequestDTO endereco,
    @NotNull(message = "É necessário aceitar os Termos de Uso e Privacidade") 
    @AssertTrue(message = "É necessário aceitar os Termos de Uso e Privacidade")
    boolean termoPrivacidade
) {
    public Usuario toEntity(){
        Usuario usuario = new Usuario();
        usuario.setCpf(this.cpf);
        usuario.setNome(this.nome);
        usuario.setDataNascimento(this.dataNascimento);
        usuario.setEmail(this.email);
        usuario.setTelefone(this.telefone);
        usuario.setSenha(this.senha);
        usuario.setGenero(this.genero);
        usuario.setPontosFidelidade(0);
  
        if(this.endereco != null){
            Endereco enderecoEntity = this.endereco.toEntity();
            enderecoEntity.setPrincipal(true);
            enderecoEntity.setUsuario(usuario);
            usuario.getEnderecos().add(enderecoEntity);
        }

        return usuario;
    }
}
