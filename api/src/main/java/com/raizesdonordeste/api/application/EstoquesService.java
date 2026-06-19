package com.raizesdonordeste.api.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raizesdonordeste.api.application.exception.ProdutoIndisponivelException;
import com.raizesdonordeste.api.domain.EstoqueIngrediente;
import com.raizesdonordeste.api.domain.EstoqueProduto;
import com.raizesdonordeste.api.domain.IngredienteProduto;
import com.raizesdonordeste.api.domain.ItemPedido;
import com.raizesdonordeste.api.domain.Unidade;

@Service
@Transactional(readOnly = true)
public class EstoquesService {

    public void consultarDisponibilidadeProdutos(Unidade unidade, List<ItemPedido> itens){
        List<EstoqueProduto> estoquePUnidade = unidade.getEstoquesProdutos();
        List<EstoqueIngrediente> estoqueIUnidade = unidade.getEstoquesIngredientes();

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
    }

    @Transactional
    public void baixarEstoques(Unidade unidade, List<ItemPedido> itens){
        List<EstoqueProduto> estoquePUnidade = unidade.getEstoquesProdutos();
        List<EstoqueIngrediente> estoqueIUnidade = unidade.getEstoquesIngredientes();

        // Transforma os estoque em Maps para facilitar consulta
        Map<Long, EstoqueProduto> mapEstoquesProdutos = estoquePUnidade.stream()
            .collect(Collectors.toMap(e -> e.getProduto().getId(), e -> e));
        Map<Long, EstoqueIngrediente> mapEstoquesIngredientes = estoqueIUnidade.stream()
            .collect(Collectors.toMap(e -> e.getIngrediente().getId(), e -> e)); 

        for(ItemPedido item : itens){
            if(item.getProduto().isExigePreparo()){
                for(IngredienteProduto ingredienteProduto : item.getProduto().getIngredientesProduto()){
                    EstoqueIngrediente estoqueI = mapEstoquesIngredientes.get(ingredienteProduto.getIngrediente().getId());

                    estoqueI.baixarEstoque(item.getQuantidade() * ingredienteProduto.getQuantidade());
                }
            }else {
                EstoqueProduto estoqueP = mapEstoquesProdutos.get(item.getProduto().getId());
                estoqueP.baixarEstoque(item.getQuantidade());
            }
        }
    }
}
