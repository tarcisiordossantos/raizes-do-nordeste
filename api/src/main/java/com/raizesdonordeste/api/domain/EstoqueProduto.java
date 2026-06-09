package com.raizesdonordeste.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.raizesdonordeste.api.exception.ProdutoIndisponivelException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "estoque_produto")
public class EstoqueProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @PositiveOrZero
    private int quantidadeAtual;
    @Column
    @PositiveOrZero
    private int quantidadeMinima;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    @JsonIgnoreProperties({"estoquesProdutos","itensPedido","produtosCardapio","ingredientesProduto"})
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "unidade_id")
    @JsonBackReference
    private Unidade unidade;

    public void baixarEstoque(int qtd) {
        if (qtd > this.quantidadeAtual){
            throw new ProdutoIndisponivelException(
                "Estoque insuficiente. Estoque disponível na Unidade: " + this.quantidadeAtual
            );
        } else {
            this.quantidadeAtual -= qtd;
        }
    }

    public void recarregarEstoque(int qtd) {
        this.quantidadeAtual += qtd;
    }

    public String abaixoDoMinimo() {
        if (this.quantidadeAtual < this.quantidadeMinima){
            return "Estoque abaixo do mínimo. Necessário repô-lo.";
        } else {
            return "Estoque adequado. Não é necessário reposição";
        }
    }
}
