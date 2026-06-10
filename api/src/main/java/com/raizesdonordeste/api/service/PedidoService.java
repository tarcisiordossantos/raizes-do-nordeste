package com.raizesdonordeste.api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raizesdonordeste.api.domain.ItemPedido;
import com.raizesdonordeste.api.domain.Pedido;
import com.raizesdonordeste.api.domain.Produto;
import com.raizesdonordeste.api.domain.Unidade;
import com.raizesdonordeste.api.domain.Usuario;
import com.raizesdonordeste.api.dto.PedidoRequestDTO;
import com.raizesdonordeste.api.dto.PedidoResponseDTO;
import com.raizesdonordeste.api.exception.FalhaNoPagamentoException;
import com.raizesdonordeste.api.gateway.PagamentoGateway;
import com.raizesdonordeste.api.repository.PedidoRepository;
import com.raizesdonordeste.api.repository.ProdutoRepository;
import com.raizesdonordeste.api.repository.UnidadeRepository;
import com.raizesdonordeste.api.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final UnidadeRepository unidadeRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstoquesService estoquesService;
    private final CardapioService cardapioService;
    private final PagamentoGateway pagamentoGateway;


    PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository, UnidadeRepository unidadeRepository, UsuarioRepository usuarioRepository, EstoquesService estoquesService, CardapioService cardapioService, PagamentoGateway pagamentoGateway)
    {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.unidadeRepository = unidadeRepository;
        this.usuarioRepository = usuarioRepository;
        this.estoquesService = estoquesService;
        this.cardapioService = cardapioService;
        this.pagamentoGateway = pagamentoGateway;
    }

    @Transactional
    public PedidoResponseDTO realizarPedido(PedidoRequestDTO dto){

        //1. Recupera Unidade que atende o pedido
        Unidade unidade = unidadeRepository.findById(dto.unidadeId())
            .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado unidade com ID" + dto.unidadeId()));

        //2. Recupera Usuario que realizou o pedido
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
            .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado usuário com ID" + dto.usuarioId()));

        //3. Criar lista com todos os Ids dos produtos
        List<Long> idsProdutos = dto.itensPedido().stream()
            .map(item -> item.produtoId()).toList();
        
        //4. Pegar os produtos no banco de dados
        List<Produto> produtos = produtoRepository.findAllById(idsProdutos);

        //5. Cria uma lista de ItemPedido com os produtos
        List<ItemPedido> itens = dto.itensPedido().stream()
            .map(item -> {
                Produto produto = produtos.stream()
                    .filter(p -> p.getId().equals(item.produtoId()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado produto com ID" + item.produtoId()));
                return item.toEntity(produto);
            }).toList();
        
        //6. Verificar disponibilidade dos produtos no cardápio ativo da unidade
        cardapioService.consultarDisponibilidadeProdutos(unidade, itens);

        //7. Verificar disponibilidade dos produtos nos estoques (produtos e ingredientes) da unidade
        estoquesService.consultarDisponibilidadeProdutos(unidade, itens);
        
        //8. Cria a entidade Pedido para ser salva no Banco de Dados
        Pedido novoPedido = dto.toEntity(usuario, unidade, itens);
        
        //9. Confirmação do pagamento (mock)
        boolean confirmacaoPagamento = pagamentoGateway.validarPagamento(novoPedido);
        if (!confirmacaoPagamento){
            throw new FalhaNoPagamentoException("Houve uma falha na tentativa de pagamento");
        } else {
            novoPedido.getPagamentos().getFirst().registrarPagamento();
        }

        //10. Dar baixa nos estoques da Unidade
        estoquesService.baixarEstoques(unidade, itens);

        //11. Salva novo pedido
        pedidoRepository.save(novoPedido);

        return PedidoResponseDTO.fromEntity(novoPedido);
    }

    public PedidoResponseDTO consultarPedidoPorId(Long id){
        Pedido pedido = pedidoRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado pedido com ID " + id));

        return PedidoResponseDTO.fromEntity(pedido);
    }

    public List<PedidoResponseDTO> consultarPedidosUnidade(Long id){
        List<Pedido> pedidos = pedidoRepository.findByUnidadeId(id);
        List<PedidoResponseDTO> pedidosDTO = pedidos.stream()
            .map(p -> PedidoResponseDTO.fromEntity(p)).toList();
        
        return pedidosDTO;
    }

    public List<PedidoResponseDTO> consultarPedidosUsuario(Long id){
        List<Pedido> pedidos = pedidoRepository.findByUsuarioId(id);
        List<PedidoResponseDTO> pedidosDTO = pedidos.stream()
            .map(p -> PedidoResponseDTO.fromEntity(p)).toList();
        
        return pedidosDTO;
    }
}
