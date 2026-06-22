package com.raizesdonordeste.api.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raizesdonordeste.api.api.dto.LoginRequestDTO;
import com.raizesdonordeste.api.api.dto.LoginResponseDTO;
import com.raizesdonordeste.api.api.dto.UsuarioSumarioDTO;
import com.raizesdonordeste.api.domain.Usuario;
import com.raizesdonordeste.api.infrastructure.security.TokenService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoint para login e geração de tokens")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/login")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token Gerado com Sucesso"),
        @ApiResponse(responseCode = "401", description = "Credenciais Inválidas", content = @Content)
    })
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto){
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(dto.cpf().replaceAll("\\D", ""), dto.senha());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var usuario = (Usuario) auth.getPrincipal();
            String token = tokenService.gerarToken(usuario);

            List<String> nomesPerfis = usuario.getPerfis().stream()
                .map(perfil -> perfil.getNome().toUpperCase())
                .toList();

            var UsuarioSumario = new UsuarioSumarioDTO(usuario.getId(), usuario.getNome(), nomesPerfis);
            var response = new LoginResponseDTO(token, "Bearer", 7200L, UsuarioSumario);

            log.info("[AUDITORIA] Autenticacao realizada com sucesso para o usuario {}", dto.cpf());

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e){
            log.warn("[AUDITORIA] Tentativa de login INVALIDA para o e-mail {}", dto.cpf());
            throw e;
        }
    }

}
