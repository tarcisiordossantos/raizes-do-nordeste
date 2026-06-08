package com.raizesdonordeste.api.service;

import com.raizesdonordeste.api.repository.PerfilRepository;
import com.raizesdonordeste.api.repository.UnidadeRepository;
import com.raizesdonordeste.api.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.stereotype.Service;

import com.raizesdonordeste.api.domain.ConsentimentoLgpd;
import com.raizesdonordeste.api.domain.Perfil;
import com.raizesdonordeste.api.domain.Usuario;
import com.raizesdonordeste.api.dto.UsuarioRequestDTO;
import com.raizesdonordeste.api.dto.UsuarioResponseDTO;
import com.raizesdonordeste.api.dto.UsuarioUpdateDTO;
import com.raizesdonordeste.api.exception.CadastroDuplicadoException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    //private final BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();

    UsuarioService(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository, UnidadeRepository unidadeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
    }

    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto){

        // Bloquear cadastro de mais de um usuário com o mesmo CPF
        if (usuarioRepository.existsByCpf(dto.cpf())){
            throw new CadastroDuplicadoException("Este CPF já está cadastrado no sistema.");
        }

        // Bloquear cadastro de mais de um usuário com o mesmo E-mail
        if (usuarioRepository.existsByEmail(dto.email())){
            throw new CadastroDuplicadoException("Este E-mail já está cadastrado no sistema.");
        }

        // Converter o DTO em uma Entidade Usuario
        Usuario novoUsuario = dto.toEntity();


        //Criptografar a senha com BCrypt
        //String senhaCriptografada = codificador.encode(novoUsuario.getSenha());
        //novoUsuario.setSenha(senhaCriptografada);

        // Usuario deve receber inicialmente o Perfil "CLIENTE"
        Perfil perfilCliente = perfilRepository.findByNome("CLIENTE")
        .orElseThrow(() -> new EntityNotFoundException("Perfil CLIENTE não localizado no banco de dados."));
        
        novoUsuario.getPerfis().add(perfilCliente);

        // Vincular o consentimento principal (Termos de Uso e Privacidade) ao Usuario
        ConsentimentoLgpd consentimento = new ConsentimentoLgpd();
        consentimento.fornecerConsentimento("TERMOS DE USO E PRIVACIDADE", dto.termoPrivacidade());
        consentimento.setUsuario(novoUsuario);
        novoUsuario.getConsentimentos().add(consentimento);

        usuarioRepository.save(novoUsuario);
        return UsuarioResponseDTO.fromEntity(novoUsuario);
    }

    public UsuarioResponseDTO consultarPorId(Long id){
        Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado usuário com ID " + id));

        return UsuarioResponseDTO.fromEntity(usuario);
    }

    public List<UsuarioResponseDTO> listarTodos(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioResponseDTO> usuariosDTO = usuarios.stream()
            .map(u -> UsuarioResponseDTO.fromEntity(u)).toList();

        return usuariosDTO;
    }

    @Transactional
    public void deletarPorId(Long id){
        if(!usuarioRepository.existsById(id)){
            throw new EntityNotFoundException("Não foi encontrado usuário com ID " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Transactional
    public UsuarioResponseDTO atualizarCadastro(Long id,  UsuarioUpdateDTO dto){
        Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado usuário com ID " + id));      

        // Bloquear cadastro de mais de um usuário com o mesmo E-mail
        if (usuarioRepository.existsByEmail(dto.email())){
                    throw new CadastroDuplicadoException("Este E-mail já está cadastrado no sistema.");
        }

        usuario.alterarInformacoes(dto);
        
        usuarioRepository.save(usuario);
    
        return UsuarioResponseDTO.fromEntity(usuario);
    }
}
