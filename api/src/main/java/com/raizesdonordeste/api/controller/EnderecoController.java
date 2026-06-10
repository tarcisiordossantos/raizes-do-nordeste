package com.raizesdonordeste.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios/{usuarioId}/enderecos")
public class EnderecoController {
    private final EnderecoService enderecoService;

    EnderecoController(EnderecoService enderecoService){
        this.enderecoService = enderecoService;
    }

    @PostMapping
    public ResponseEntity<EnderecoResponseDTO> cadastrarEndereco(@Valid @RequestBody EnderecoRequestDTO dto, @PathVariable Long usuarioId) {
        EnderecoResponseDTO endereco = enderecoService.cadastrar(dto, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(endereco);
    }

    @GetMapping
    public ResponseEntity<List<EnderecoResponseDTO>> listarTodos(@PathVariable Long usuarioId) {
        List<EnderecoResponseDTO> enderecos = enderecoService.listarTodos(usuarioId);
        return ResponseEntity.ok(enderecos);
    }

    @PutMapping("/{enderecoId}")
    public ResponseEntity<EnderecoResponseDTO> atualizarEndereco(@PathVariable Long usuarioId, @PathVariable Long enderecoId, 
        @Valid @RequestBody EnderecoRequestDTO dto) {

        EnderecoResponseDTO endereco = enderecoService.atualizarCadastro(usuarioId, enderecoId, dto);
        return ResponseEntity.ok(endereco);
    }

    @DeleteMapping("/{enderecoId}")
    public ResponseEntity<Void> deletar(@PathVariable Long usuarioId, @PathVariable Long enderecoId){
        enderecoService.deletarPorId(usuarioId, enderecoId);
        return ResponseEntity.noContent().build();
    }
}
