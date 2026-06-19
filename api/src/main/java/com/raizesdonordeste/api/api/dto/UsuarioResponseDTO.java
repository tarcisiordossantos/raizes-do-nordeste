package com.raizesdonordeste.api.api.dto;

import java.time.LocalDate;
import java.util.List;

import com.raizesdonordeste.api.domain.Usuario;

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
    public static UsuarioResponseDTO fromEntity(Usuario entidade){
        return new UsuarioResponseDTO(
            entidade.getId(), 
            entidade.getCpf(),
            entidade.getNome(),
            entidade.getDataNascimento(), 
            entidade.getEmail(), 
            entidade.getTelefone(), 
            entidade.getGenero(), 
            entidade.getPontosFidelidade(), 
            entidade.getPerfis().stream()
            .map(per -> per.getNome()).toList(),
            entidade.getEnderecos().stream()
            .map(end -> EnderecoResponseDTO.fromEntity(end)).toList());
    }
}
