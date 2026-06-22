package com.raizesdonordeste.api.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "produto")
public class Produto {

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
    @Size(max = 150)
    private String categoria;
    @Column(name = "preco_base")
    @NotNull
    @Positive
    private BigDecimal precoBase;
    @Column(name = "exige_preparo")
    private boolean exigePreparo;
    @Column
    private boolean ativo;

    @OneToMany(mappedBy = "produto")
    @JsonIgnore
    private List<ItemPedido> itensPedido = new ArrayList<>();

    @OneToMany(mappedBy = "produto")
    @JsonIgnore
    private List<ProdutoCardapio> produtosCardapio  = new ArrayList<>();
    
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<IngredienteProduto> ingredientesProduto = new ArrayList<>();

    @OneToMany(mappedBy = "produto")
    @JsonIgnore
    private List<EstoqueProduto> estoquesProdutos = new ArrayList<>();
}
