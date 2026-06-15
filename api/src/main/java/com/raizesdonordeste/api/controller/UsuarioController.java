package com.raizesdonordeste.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raizesdonordeste.api.dto.UsuarioRequestDTO;
import com.raizesdonordeste.api.dto.UsuarioResponseDTO;
import com.raizesdonordeste.api.dto.UsuarioUpdateDTO;
import com.raizesdonordeste.api.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @Operation(
        summary = "Cadastrar novo usuário", 
        description = "Para testes o usuário com id 1 recebe o perfil GERENTE e os demais apenas perfil CLIENTE")
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO usuario = usuarioService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE') or #id == authentication.principal.id")
    @Operation(summary = "Consultar usuário por seu ID")
    public ResponseEntity<UsuarioResponseDTO> consultarPorId(@PathVariable Long id) {
        UsuarioResponseDTO usuario = usuarioService.consultarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Listar todos os usuários cadastrados")
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE') or #id == authentication.principal.id")
    @Operation(
        summary = "Deletar usuário por seu ID", 
        description = "Deleta o usuário que não tem pedido registrado e anomimiza o que tem pedido registrado")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE') or #id == authentication.principal.id")
    @Operation(
        summary = "Atualizar cadastro usuário por seu ID",
        description = "Não é possivel alterar o CPF do usuário e alterações de endereço devem ser feitas em rotas próprias")
    public ResponseEntity<UsuarioResponseDTO> atualizarCadastro(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateDTO dto) {
        UsuarioResponseDTO usuario = usuarioService.atualizarCadastro(id, dto);
        return ResponseEntity.ok(usuario);
    }
}
