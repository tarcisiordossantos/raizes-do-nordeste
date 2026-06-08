package com.raizesdonordeste.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "produto_cardapio")
public class ProdutoCardapio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private boolean disponivel;

    @ManyToOne
    @JoinColumn(name = "cardapio_id")
    @JsonBackReference
    private Cardapio cardapio;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    @JsonIgnoreProperties({"produtosCardapio","itensPedido","ingredientesProduto","estoquesProdutos"})
    private Produto produto;
}
