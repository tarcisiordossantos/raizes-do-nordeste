package com.raizesdonordeste.api.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raizesdonordeste.api.api.dto.EnderecoRequestDTO;
import com.raizesdonordeste.api.api.dto.EnderecoResponseDTO;
import com.raizesdonordeste.api.domain.Endereco;
import com.raizesdonordeste.api.domain.Usuario;
import com.raizesdonordeste.api.infrastructure.repository.EnderecoRepository;
import com.raizesdonordeste.api.infrastructure.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnderecoService {
    private final UsuarioRepository usuarioRepository;
    private final EnderecoRepository enderecoRepository;


    @Transactional
    public EnderecoResponseDTO cadastrar(EnderecoRequestDTO dto, Long usuarioId){
        Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado usuário com ID " + usuarioId));

        Endereco endereco = dto.toEntity();
        usuario.getEnderecos().add(endereco);
        endereco.setUsuario(usuario);

        enderecoRepository.save(endereco);
        return EnderecoResponseDTO.fromEntity(endereco);
    }

    public List<EnderecoResponseDTO> listarTodos(Long usuarioId){
        Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado usuário com ID " + usuarioId));

        List<EnderecoResponseDTO> enderecos = usuario.getEnderecos().stream()
            .map(e -> EnderecoResponseDTO.fromEntity(e)).toList();

        return enderecos;
    }

    @Transactional
    public EnderecoResponseDTO atualizarCadastro(Long usuarioId, Long enderecoId, EnderecoRequestDTO dto){
        Endereco endereco = enderecoRepository.findById(enderecoId)
        .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado endereço com ID "+ enderecoId));

        if(endereco.getUsuario() == null || !endereco.getUsuario().getId().equals(usuarioId)){
            throw new IllegalArgumentException("O endereço não pertece ao usuário informado.");
        }

        endereco.alterarEndereco(dto);
        enderecoRepository.save(endereco);

        return EnderecoResponseDTO.fromEntity(endereco);
    } 

    @Transactional
    public void deletarPorId(Long usuarioId, Long enderecoId){
        Endereco endereco = enderecoRepository.findById(enderecoId)
        .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado endereço com ID "+ enderecoId));

        if(endereco.getUsuario() == null || !endereco.getUsuario().getId().equals(usuarioId)){
            throw new IllegalArgumentException("O endereço não pertece ao usuário informado.");
        }

        enderecoRepository.delete(endereco);
    }


}
