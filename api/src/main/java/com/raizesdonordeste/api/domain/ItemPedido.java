package com.raizesdonordeste.api.domain;

import java.math.BigDecimal;

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
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "produto_pedido")
public class ItemPedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @Positive
    private int quantidade;
    @Column
    private BigDecimal subtotal;
    @Column(name = "status_produto")
    @Size(max = 50)
    private String statusProduto;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    @JsonIgnoreProperties({"itensPedido","produtosCardapio","ingredientesProduto","estoquesProdutos"})
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    @JsonBackReference
    private Pedido pedido;

    public BigDecimal calcularSubtotal() {
        subtotal = BigDecimal.ZERO;
        subtotal = produto.getPrecoBase().multiply(BigDecimal.valueOf(quantidade));
        return subtotal;
    }
}
