package com.raizesdonordeste.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raizesdonordeste.api.dto.ProdutoResponseDTO;
import com.raizesdonordeste.api.service.CardapioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/unidades")
@Tag(name = "Unidades", description = "Endpoints para gerenciamento de unidades")
public class UnidadeController {
    private final CardapioService cardapioService;

    @GetMapping("/{unidadeId}/cardapio")
    @Operation(summary = "Listar produtos do cardápio ativo na unidade")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produtos do cardápio listados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Recurso Não Encontrado", content = @Content),
        @ApiResponse(responseCode = "422", description = "Unidade inoperante: sem cardápio ativo", content = @Content)
    })
    public ResponseEntity<List<ProdutoResponseDTO>> cardapioAtivo(@PathVariable Long unidadeId){
        List<ProdutoResponseDTO> produtosCardapio = cardapioService.cardapioAtivo(unidadeId);

        return ResponseEntity.ok(produtosCardapio);
    }

}
