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
@Table(name = "estoque_ingrediente")
public class EstoqueIngrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @PositiveOrZero
    private double quantidadeAtual;
    @Column
    @PositiveOrZero
    private double quantidadeMinima;

    @ManyToOne
    @JoinColumn(name = "ingrediente_id")
    @JsonIgnoreProperties({"estoquesIngredientes","ingredientesProduto"})
    private Ingrediente ingrediente;

    @ManyToOne
    @JoinColumn(name = "unidade_id")
    @JsonBackReference
    private Unidade unidade;

    public void baixarEstoque(double qtd) {
        if (qtd > this.quantidadeAtual){
            throw new ProdutoIndisponivelException(
                "Estoque insuficiente. Estoque disponível na Unidade: " + this.quantidadeAtual
            );
        } else {
            this.quantidadeAtual -= qtd;
        }
    }

    public void recarregarEstoque(double qtd) {
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
