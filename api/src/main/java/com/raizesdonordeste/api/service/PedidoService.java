package com.raizesdonordeste.api.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.raizesdonordeste.api.domain.Cardapio;
import com.raizesdonordeste.api.domain.EstoqueIngrediente;
import com.raizesdonordeste.api.domain.EstoqueProduto;
import com.raizesdonordeste.api.domain.IngredienteProduto;
import com.raizesdonordeste.api.domain.ItemPedido;
import com.raizesdonordeste.api.domain.Pedido;
import com.raizesdonordeste.api.domain.Produto;
import com.raizesdonordeste.api.domain.Unidade;
import com.raizesdonordeste.api.domain.Usuario;
import com.raizesdonordeste.api.dto.PedidoRequestDTO;
import com.raizesdonordeste.api.dto.PedidoResponseDTO;
import com.raizesdonordeste.api.exception.ProdutoIndisponivelException;
import com.raizesdonordeste.api.exception.UnidadeSemCardapioAtivoException;
import com.raizesdonordeste.api.exception.FalhaNoPagamentoException;
import com.raizesdonordeste.api.repository.EstoqueIngredienteRepository;
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
    private final EstoqueIngredienteRepository estoqueIngredienteRepository;

    PedidoService(
        PedidoRepository pedidoRepository,
        ProdutoRepository produtoRepository,
        UnidadeRepository unidadeRepository, 
        UsuarioRepository usuarioRepository,
        EstoqueProdutoRepository estoqueProdutoRepository,
        EstoqueIngredienteRepository estoqueIngredienteRepository){
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.unidadeRepository = unidadeRepository;
        this.usuarioRepository = usuarioRepository;
        this.estoqueProdutoRepository = estoqueProdutoRepository;
        this.estoqueIngredienteRepository = estoqueIngredienteRepository;
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


        //Seleciona o cardápio ativo na Unidade
        Cardapio cardapioAtivo = unidade.getCardapios().stream()
            .filter(c -> c.isAtivo() == true ).findFirst()
            .orElseThrow(() -> new UnidadeSemCardapioAtivoException("A Unidade "+unidade.getNome()+" não possui nenhum carpádio ativo. Não foi possível concluir o pedido." ));
        
        Set<Long> idsProdutosCardapio = cardapioAtivo.getProdutosCardapio().stream()
        .map(p -> p.getProduto().getId()).collect(Collectors.toSet());
        
        //Verificar se o cliente enviou o id de algum produto não comercializado no cardápio da Unidade
        for(ItemPedido item : itens){
            if(!idsProdutosCardapio.contains(item.getProduto().getId())){
                throw new ProdutoIndisponivelException("Produto "+item.getProduto().getNome()+ " não disponível no cardápio da Unidade.");
            }
        }
        
        //Consultar estoques da Unidade
        List<EstoqueProduto> estoquePUnidade = estoqueProdutoRepository.findByUnidadeId(unidade.getId());
        List<EstoqueIngrediente> estoqueIUnidade = estoqueIngredienteRepository.findByUnidadeId(unidade.getId());

        // Transforma os estoque em Maps para facilitar consulta
        Map<Long, EstoqueProduto> mapEstoquesProdutos = estoquePUnidade.stream()
            .collect(Collectors.toMap(e -> e.getProduto().getId(), e -> e));
        Map<Long, EstoqueIngrediente> mapEstoquesIngredientes = estoqueIUnidade.stream()
            .collect(Collectors.toMap(e -> e.getIngrediente().getId(), e -> e)); 


        //Confirmar disponibilidade nos estoques de produtos e ingredientes
        for(ItemPedido item : itens){
            if(item.getProduto().isExigePreparo()){
                for(IngredienteProduto ingredienteProduto : item.getProduto().getIngredientesProduto()){
                    EstoqueIngrediente estoqueI = mapEstoquesIngredientes.get(ingredienteProduto.getIngrediente().getId());

                    double qtdExigida = item.getQuantidade() * ingredienteProduto.getQuantidade();

                    if(estoqueI == null || qtdExigida > estoqueI.getQuantidadeAtual()){
                        throw new ProdutoIndisponivelException("Estoque de ingredientes insuficiente para o produto " + item.getProduto().getNome());
                    }
                }
            } else {
                EstoqueProduto estoqueP = mapEstoquesProdutos.get(item.getProduto().getId());

                if(estoqueP == null || item.getQuantidade() > estoqueP.getQuantidadeAtual()){
                        throw new ProdutoIndisponivelException("Estoque insuficiente para o produto " + item.getProduto().getNome());
                }
            }  
        }

        //Confirmação do pagamento (mock)
        boolean confirmacaoPagamento = novoPedido.getPagamentos().getFirst().validarPagamento();
        if (!confirmacaoPagamento){
            throw new FalhaNoPagamentoException("Houve uma falha na tentativa de pagamento");
        }

        //Dar baixa no estoque da Unidade
        for(ItemPedido item : itens){
            if(item.getProduto().isExigePreparo()){
                for(IngredienteProduto ingredienteProduto : item.getProduto().getIngredientesProduto()){
                    EstoqueIngrediente estoqueI = mapEstoquesIngredientes.get(ingredienteProduto.getIngrediente().getId());

                    estoqueI.baixarEstoque(item.getQuantidade() * ingredienteProduto.getQuantidade());
                    estoqueIngredienteRepository.save(estoqueI);
                }
            }else {
                EstoqueProduto estoqueP = mapEstoquesProdutos.get(item.getProduto().getId());
                estoqueP.baixarEstoque(item.getQuantidade());
                estoqueProdutoRepository.save(estoqueP);
            }
        }

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
