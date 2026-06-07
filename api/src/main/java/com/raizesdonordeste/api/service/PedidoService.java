package com.raizesdonordeste.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.raizesdonordeste.api.domain.EstoqueProduto;
import com.raizesdonordeste.api.domain.ItemPedido;
import com.raizesdonordeste.api.domain.Pedido;
import com.raizesdonordeste.api.domain.Produto;
import com.raizesdonordeste.api.domain.Unidade;
import com.raizesdonordeste.api.domain.Usuario;
import com.raizesdonordeste.api.dto.PedidoRequestDTO;
import com.raizesdonordeste.api.dto.PedidoResponseDTO;
import com.raizesdonordeste.api.exception.EstoqueInsuficienteException;
import com.raizesdonordeste.api.exception.FalhaNoPagamentoException;
import com.raizesdonordeste.api.repository.EstoqueProdutoRepository;
import com.raizesdonordeste.api.repository.PedidoRepository;
import com.raizesdonordeste.api.repository.ProdutoRepository;
import com.raizesdonordeste.api.repository.UnidadeRepository;
import com.raizesdonordeste.api.repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final UnidadeRepository unidadeRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstoqueProdutoRepository estoqueProdutoRepository;

    PedidoService(
        PedidoRepository pedidoRepository,
        ProdutoRepository produtoRepository,
        UnidadeRepository unidadeRepository, 
        UsuarioRepository usuarioRepository,
        EstoqueProdutoRepository estoqueProdutoRepository){
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.unidadeRepository = unidadeRepository;
        this.usuarioRepository = usuarioRepository;
        this.estoqueProdutoRepository = estoqueProdutoRepository;
    }

    @Transactional
    public PedidoResponseDTO realizarPedido(PedidoRequestDTO dto){

        //Recupera Unidade que atende o pedido
        Unidade unidade = unidadeRepository.findById(dto.unidadeId())
            .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado unidade com ID" + dto.unidadeId()));

        //Recupera Usuario que realizou o pedido
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
            .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado usuário com ID" + dto.usuarioId()));

        //Criar lista com todos os Ids dos produtos
        List<Long> idsProdutos = dto.itensPedido().stream()
            .map(item -> item.produtoId()).toList();
        
        //Pegar os produtos no banco de dados
        List<Produto> produtos = produtoRepository.findAllById(idsProdutos);

        //Cria uma lista de ItemPedido com os produtos
        List<ItemPedido> itens = dto.itensPedido().stream()
            .map(item -> {
                Produto produto = produtos.stream()
                    .filter(p -> p.getId().equals(item.produtoId()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Não foi encontrado produto com ID" + item.produtoId()));
                return item.toEntity(produto);
            }).toList();
        
        //Cria a entidade Pedido para ser salva no Banco de Dados
        Pedido novoPedido = dto.toEntity(usuario, unidade, itens);
        
        //Consultar estoques da Unidade
        List<EstoqueProduto> estoqueUnidade = estoqueProdutoRepository.findByUnidadeId(unidade.getId());

        //Confirmar disponibilidade no estoque
        for(ItemPedido item : itens){
            boolean disponivelNaUnidade = false;
            for(EstoqueProduto estoque : estoqueUnidade){
                if(item.getProduto().getId().equals(estoque.getProduto().getId())){
                    disponivelNaUnidade = true;

                    //Valida quantidade no estoque
                    if(item.getQuantidade() > estoque.getQuantidadeAtual()){
                        throw new EstoqueInsuficienteException("Estoque insuficiente para o produto "+ estoque.getProduto().getNome() +". Estoque disponível "+ estoque.getQuantidadeAtual());
                    }
                }
            }
            //Valida disponibilidade na unidade
            if(!disponivelNaUnidade){
                throw new EntityNotFoundException("Produto "+ item.getProduto().getNome() + " não está sendo comercializado na unidade.");
            } 
        }

        //Confirmação do pagamento (mock)
        boolean confirmacaoPagamento = novoPedido.getPagamentos().getFirst().validarPagamento();
        if (!confirmacaoPagamento){
            throw new FalhaNoPagamentoException("Houve uma falha na tentativa de pagamento");
        }

        //Dar baixa no estoque da Unidade
        for(ItemPedido item : itens){
            for(EstoqueProduto estoque : estoqueUnidade){
                if(item.getProduto().getId() == estoque.getProduto().getId()){
                    estoque.baixarEstoque(item.getQuantidade());
                }
            }
        }

        pedidoRepository.save(novoPedido);

        return PedidoResponseDTO.fromEntity(novoPedido);
    }

}
