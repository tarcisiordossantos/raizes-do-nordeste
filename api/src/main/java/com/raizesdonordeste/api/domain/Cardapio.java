package com.raizesdonordeste.api.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cardapio")
public class Cardapio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotBlank
    @Size(max = 150)
    private String nome;
    @Column
    @Size(max = 255)
    private String descricao;
    @Column
    private boolean ativo;

    @ManyToOne
    @JoinColumn(name = "unidade_id")
    private Unidade unidade;

    @OneToMany(mappedBy = "cardapio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoCardapio> produtosCardapio = new ArrayList<>();

    public void adicionarProduto(ProdutoCardapio produtoCardapio) {
        produtosCardapio.add(produtoCardapio);
        produtoCardapio.setCardapio(this);
    }

    public void retirarProduto(ProdutoCardapio produtoCardapio) {
        produtosCardapio.remove(produtoCardapio);
        produtoCardapio.setCardapio(null);
    }
}
