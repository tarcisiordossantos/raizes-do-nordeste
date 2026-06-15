package com.raizesdonordeste.api.service;

import com.raizesdonordeste.api.repository.PerfilRepository;
import com.raizesdonordeste.api.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raizesdonordeste.api.domain.ConsentimentoLgpd;
import com.raizesdonordeste.api.domain.Perfil;
import com.raizesdonordeste.api.domain.Usuario;
import com.raizesdonordeste.api.dto.UsuarioRequestDTO;
import com.raizesdonordeste.api.dto.UsuarioResponseDTO;
import com.raizesdonordeste.api.dto.UsuarioUpdateDTO;
import com.raizesdonordeste.api.exception.CadastroDuplicadoException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();


    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto){

        // Bloquear cadastro de mais de um usuário com o mesmo CPF
        String cpfLimpo = dto.cpf().replaceAll("\\D", "");
        if (usuarioRepository.existsByCpf(cpfLimpo)){
            throw new CadastroDuplicadoException("Este CPF já está cadastrado no sistema.");
        }

        // Bloquear cadastro de mais de um usuário com o mesmo E-mail
        if (usuarioRepository.existsByEmail(dto.email())){
            throw new CadastroDuplicadoException("Este E-mail já está cadastrado no sistema.");
        }

        // Converter o DTO em uma Entidade Usuario
        Usuario novoUsuario = dto.toEntity();


        //Criptografar a senha com BCrypt
        String senhaCriptografada = codificador.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        // Usuario deve receber inicialmente o Perfil "CLIENTE"
        Perfil perfilCliente = perfilRepository.findByNome("CLIENTE")
            .orElseThrow(() -> new EntityNotFoundException("Perfil CLIENTE não localizado no banco de dados."));
        novoUsuario.getPerfis().add(perfilCliente);

        //Primeiro usuário recebe o perfil de GERENTE
        if (usuarioRepository.count() == 0){
            Perfil perfilGerente = perfilRepository.findByNome("GERENTE")
                .orElseThrow(() -> new EntityNotFoundException("Perfil GERENTE não localizado no banco de dados."));
            novoUsuario.getPerfis().add(perfilGerente);
        }

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
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado usuário com ID " + id));
       
        if (!usuario.getPedidos().isEmpty()){
            String cpfAnonimo = String.format("%011d", usuario.getId());

            usuario.setCpf(cpfAnonimo);
            usuario.setNome("Anonimizado (LGPD)");
            usuario.setDataNascimento(LocalDate.now());
            usuario.setEmail("anonimizado"+id+"@email.com");
            usuario.setTelefone(null);
            usuario.setSenha("**********");
            usuario.setGenero(null);
            usuario.setPontosFidelidade(0);
            usuario.getEnderecos().clear();
        } else {
            usuarioRepository.deleteById(id);
        }
    }

    @Transactional
    public UsuarioResponseDTO atualizarCadastro(Long id,  UsuarioUpdateDTO dto){
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado usuário com ID " + id));      

        // Bloquear cadastro de mais de um usuário com o mesmo E-mail
        if (usuarioRepository.existsByEmail(dto.email())){
                    throw new CadastroDuplicadoException("Este E-mail já está cadastrado no sistema.");
        }

        String senhaCriptografada = "";
        if(dto.senha() != null && !dto.senha().isBlank()){
            senhaCriptografada = codificador.encode(dto.senha());
        }
        
        usuario.alterarInformacoes(dto, senhaCriptografada);
        
        usuarioRepository.save(usuario);
    
        return UsuarioResponseDTO.fromEntity(usuario);
    }
}
