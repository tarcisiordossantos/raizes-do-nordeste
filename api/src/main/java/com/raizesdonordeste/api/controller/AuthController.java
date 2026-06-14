package com.raizesdonordeste.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raizesdonordeste.api.domain.Usuario;
import com.raizesdonordeste.api.dto.LoginRequestDTO;
import com.raizesdonordeste.api.dto.LoginResponseDTO;
import com.raizesdonordeste.api.dto.UsuarioSumarioDTO;
import com.raizesdonordeste.api.security.TokenService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoint para login e geração de tokens")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto){
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var usuario = (Usuario) auth.getPrincipal();
        String token = tokenService.gerarToken(usuario);

        List<String> nomesPerfis = usuario.getPerfis().stream()
            .map(perfil -> perfil.getNome().toUpperCase())
            .toList();

        var UsuarioSumario = new UsuarioSumarioDTO(usuario.getId(), usuario.getNome(), nomesPerfis);
        var response = new LoginResponseDTO(token, "Bearer", 7200L, UsuarioSumario);

        return ResponseEntity.ok(response);
    }

}
