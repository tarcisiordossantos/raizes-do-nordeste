package com.raizesdonordeste.api.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.raizesdonordeste.api.infrastructure.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutenticacaoService implements UserDetailsService{

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException
    {
        return usuarioRepository.findByCpf(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com e-mail: "+ username));
    }

}
