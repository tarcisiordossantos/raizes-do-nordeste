package com.raizesdonordeste.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raizesdonordeste.api.dto.PedidoRequestDTO;
import com.raizesdonordeste.api.dto.PedidoResponseDTO;
import com.raizesdonordeste.api.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Endpoints para gerenciamento dos pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    PedidoController(PedidoService pedidoService){
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo pedido")
    public ResponseEntity<PedidoResponseDTO> realizarPedido(@Valid @RequestBody PedidoRequestDTO dto){
        PedidoResponseDTO pedido = pedidoService.realizarPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar pedido cadastrado pelo ID")
    public ResponseEntity<PedidoResponseDTO> consultarPedidoPorId(@PathVariable Long id){
        PedidoResponseDTO pedido = pedidoService.consultarPedidoPorId(id);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping
    @Operation(summary = "Cosultar todos os pedidos do Usuário ou da Unidade")
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidos(
        @RequestParam(value = "unidadeId", required = false) Long unidadeId,
        @RequestParam(value = "usuarioId", required = false) Long usuarioId){
        
        if(unidadeId != null){
            List<PedidoResponseDTO> pedidos = pedidoService.consultarPedidosUnidade(unidadeId);
            return ResponseEntity.ok(pedidos);
        } 

        if(usuarioId != null){
            List<PedidoResponseDTO> pedidos = pedidoService.consultarPedidosUsuario(usuarioId);
            return ResponseEntity.ok(pedidos);
        }
        return ResponseEntity.ok(List.of());
    }
}
