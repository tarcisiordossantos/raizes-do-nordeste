package com.raizesdonordeste.api.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raizesdonordeste.api.domain.Cardapio;
import com.raizesdonordeste.api.domain.ItemPedido;
import com.raizesdonordeste.api.domain.Unidade;
import com.raizesdonordeste.api.dto.ProdutoResponseDTO;
import com.raizesdonordeste.api.exception.ProdutoIndisponivelException;
import com.raizesdonordeste.api.exception.UnidadeSemCardapioAtivoException;
import com.raizesdonordeste.api.repository.CardapioRepository;
import com.raizesdonordeste.api.repository.UnidadeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CardapioService {
    private final UnidadeRepository unidadeRepository;
    private final CardapioRepository cardapioRepository;

    public void consultarDisponibilidadeProdutos(Unidade unidade, List<ItemPedido> itens){
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
    }

    public List<ProdutoResponseDTO> cardapioAtivo(Long unidadeId){
        if(!unidadeRepository.existsById(unidadeId)){
            throw new EntityNotFoundException("Não foi encontrado unidade com ID " + unidadeId);
        }

        Cardapio cardapioAtivo = cardapioRepository.findByUnidadeIdAndAtivoTrue(unidadeId)
            .orElseThrow(() -> new UnidadeSemCardapioAtivoException("A Unidade ID " + unidadeId + " não possui nenhum carpádio ativo."));

        List<ProdutoResponseDTO> produtosCardapio = cardapioAtivo.getProdutosCardapio().stream()
            .map(p -> ProdutoResponseDTO.fromEntity(p.getProduto())).toList();
        
        
        return produtosCardapio;
    }
}
