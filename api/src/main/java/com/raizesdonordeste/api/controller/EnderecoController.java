package com.raizesdonordeste.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raizesdonordeste.api.dto.EnderecoRequestDTO;
import com.raizesdonordeste.api.dto.EnderecoResponseDTO;
import com.raizesdonordeste.api.service.EnderecoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios/{usuarioId}/enderecos")
@Tag(name = "Endereços", description = "Endpoints para gerenciamento dos endereços dos usuários")
public class EnderecoController {
    private final EnderecoService enderecoService;

    EnderecoController(EnderecoService enderecoService){
        this.enderecoService = enderecoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('GERENTE') or #usuarioId == authentication.principal.id")
    @Operation(summary = "Cadastrar novo endereço para o usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Endereço Cadastrado"),
        @ApiResponse(responseCode = "400", description = "Não preenchimento de campo obrigatório ou preenchimento incorreto", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado por não estar autenticado ou estar tentando acessar/modificar informações de outro usuário, salvo GERENTE (permissão total)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Recurso Não Encontrado", content = @Content)
    })
    public ResponseEntity<EnderecoResponseDTO> cadastrarEndereco(@Valid @RequestBody EnderecoRequestDTO dto, @PathVariable Long usuarioId) {
        EnderecoResponseDTO endereco = enderecoService.cadastrar(dto, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(endereco);
    }

    @GetMapping
    @PreAuthorize("hasRole('GERENTE') or #usuarioId == authentication.principal.id")
    @Operation(summary = "Consultar todos os endereços do usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Endereços encontrados com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado por não estar autenticado ou estar tentando acessar/modificar informações de outro usuário, salvo GERENTE (permissão total)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Recurso Não Encontrado", content = @Content)
    })
    public ResponseEntity<List<EnderecoResponseDTO>> listarTodos(@PathVariable Long usuarioId) {
        List<EnderecoResponseDTO> enderecos = enderecoService.listarTodos(usuarioId);
        return ResponseEntity.ok(enderecos);
    }

    @PutMapping("/{enderecoId}")
    @PreAuthorize("hasRole('GERENTE') or #usuarioId == authentication.principal.id")
    @Operation(summary = "Atualizar endereço do usuário pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cadastro atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Não preenchimento de campo obrigatório ou preenchimento incorreto", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado por não estar autenticado ou estar tentando acessar/modificar informações de outro usuário, salvo GERENTE (permissão total)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Recurso Não Encontrado", content = @Content)
    })
    public ResponseEntity<EnderecoResponseDTO> atualizarEndereco(@PathVariable Long usuarioId, @PathVariable Long enderecoId, 
        @Valid @RequestBody EnderecoRequestDTO dto) {

        EnderecoResponseDTO endereco = enderecoService.atualizarCadastro(usuarioId, enderecoId, dto);
        return ResponseEntity.ok(endereco);
    }

    @DeleteMapping("/{enderecoId}")
    @PreAuthorize("hasRole('GERENTE') or #usuarioId == authentication.principal.id")
    @Operation(summary = "Deletar endereço do usuário pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Endereço deletado"),
        @ApiResponse(responseCode = "400", description = "Não preenchimento de campo obrigatório ou preenchimento incorreto", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acesso não autorizado por não estar autenticado ou estar tentando acessar/modificar informações de outro usuário, salvo GERENTE (permissão total)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Recurso Não Encontrado", content = @Content)
    })
    public ResponseEntity<Void> deletar(@PathVariable Long usuarioId, @PathVariable Long enderecoId){
        enderecoService.deletarPorId(usuarioId, enderecoId);
        return ResponseEntity.noContent().build();
    }
}
