package com.raizesdonordeste.api.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raizesdonordeste.api.api.dto.PedidoRequestDTO;
import com.raizesdonordeste.api.api.dto.PedidoResponseDTO;
import com.raizesdonordeste.api.api.dto.PedidoUpdateDTO;
import com.raizesdonordeste.api.application.PedidoService;
import com.raizesdonordeste.api.domain.Usuario;
import com.raizesdonordeste.api.domain.enuns.CanalOrigem;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @PreAuthorize("hasAnyRole('GERENTE', 'ATENDENTE') or #dto.usuarioId == authentication.principal.id")
    @Operation(
        summary = "Cadastrar novo pedido", 
        description = "O pedido pode ser realizado por GERENTE, ATENDENTE ou o próprio CLIENTE.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido Cadastrado"),
        @ApiResponse(responseCode = "400", description = "Não preenchimento de campo obrigatório ou preenchimento incorreto", content = @Content),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado por não estar autenticado ou com token expirado/inválido", content = @Content),
        @ApiResponse(responseCode = "403", description = "Perfil do usuário não permite acesso a esta rota por estar tentando acessar/modificar informações de outro usuário e não ter perfil GERENTE (permissão total) ou ATENDENTE", content = @Content),
        @ApiResponse(responseCode = "404", description = "Recurso Não Encontrado", content = @Content),
        @ApiResponse(responseCode = "422", description = "Falha no Processamento do Pagamento", content = @Content)
    })
    public ResponseEntity<PedidoResponseDTO> realizarPedido(@Valid @RequestBody PedidoRequestDTO dto){
        PedidoResponseDTO pedido = pedidoService.realizarPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }



    @GetMapping("/meus")
    @PreAuthorize("hasRole('CLIENTE')")
    @Operation(summary = "Consultar pedidos cadastrado do usuário autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedidos encontrados com sucesso"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado por não estar autenticado ou com token expirado/inválido", content = @Content)
    })
    public ResponseEntity<Page<PedidoResponseDTO>> consultarPedidosUsuario(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending()); 
        
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        Page<PedidoResponseDTO> pedidos = pedidoService.consultarPedidosUsuario(usuarioLogado.getId(), pageable);
        return ResponseEntity.ok(pedidos);
    }



    @GetMapping
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(
        summary = "Consultar todos os pedidos por Unidade, Canal ou Usuário",
        description = "Funcionalidade exclusiva para usuários com perfil GERENTE. A pesquisa por Unidade pode ser combinada com Canal ou Usuário.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedidos encontrados com sucesso"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado por não estar autenticado ou com token expirado/inválido", content = @Content),
        @ApiResponse(responseCode = "403", description = "Perfil do usuário não permite acesso a esta rota por não ter perfil GERENTE (permissão total)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Recurso Não Encontrado", content = @Content)
    })
    public ResponseEntity<Page<PedidoResponseDTO>> listarPedidos(
        @RequestParam(required = false) Long unidadeId,
        @RequestParam(required = false) Long usuarioId,
        @RequestParam(required = false) CanalOrigem canal,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size){
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending()); 
        
        if(unidadeId != null && usuarioId != null){
            Page<PedidoResponseDTO> pedidos = pedidoService.consultarPorUnidadeEUsuario(unidadeId, usuarioId, pageable);
            return ResponseEntity.ok(pedidos);
        } else if (unidadeId != null && canal != null){
            Page<PedidoResponseDTO> pedidos = pedidoService.consultarPorUnidadeECanal(unidadeId, canal, pageable);
            return ResponseEntity.ok(pedidos);
        } else if (canal != null){
            Page<PedidoResponseDTO> pedidos = pedidoService.consultarPorCanal(canal, pageable);
            return ResponseEntity.ok(pedidos);
        } else if(unidadeId != null){
            Page<PedidoResponseDTO> pedidos = pedidoService.consultarPedidosUnidade(unidadeId, pageable);
            return ResponseEntity.ok(pedidos);
        } else if (usuarioId != null){
            Page<PedidoResponseDTO> pedidos = pedidoService.consultarPedidosUsuario(usuarioId, pageable);
            return ResponseEntity.ok(pedidos);
        }else {
            return ResponseEntity.ok(Page.empty());
        }
        
    }



    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE') or #dto.usuarioId == authentication.principal.id")
    @Operation(
        summary = "Atualizar observação do pedido ou cancelá-lo",
        description = "Apenas é possível cancelamento de pedidos com status AGUARDANDO_PAGAMENTO ou PAGAMENTO_CONFIRMADO, "
                    +" e que seja o último pedido realizado pelo usuário." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido atualizado/cancelado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Não preenchimento de campo obrigatório ou preenchimento incorreto", content = @Content),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado por não estar autenticado ou com token expirado/inválido", content = @Content),
        @ApiResponse(responseCode = "403", description = "Perfil do usuário não permite acesso a esta rota por estar tentando acessar/modificar informações de outro usuário e não ter perfil GERENTE (permissão total)", content = @Content),
        @ApiResponse(responseCode = "404", description = "Recurso Não Encontrado", content = @Content),
        @ApiResponse(responseCode = "422", description = "Regra de Negócio Violada", content = @Content)
    })
    public ResponseEntity<PedidoResponseDTO> atualizarPedido(@PathVariable Long id, @Valid @RequestBody PedidoUpdateDTO dto){
        PedidoResponseDTO pedido = pedidoService.atualizarPedido(id, dto);

        return ResponseEntity.ok(pedido);
    }
    
}
