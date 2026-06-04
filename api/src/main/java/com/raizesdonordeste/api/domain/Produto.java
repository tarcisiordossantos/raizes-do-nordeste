package com.raizesdonordeste.api.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    @Column
    @NotNull
    @Positive
    private BigDecimal precoBase;
    @Column
    private boolean exigePreparo;
    @Column
    private boolean ativo;

    @OneToMany(mappedBy = "produto")
    private List<ProdutoCardapio> produtosCardapio  = new ArrayList<>();
    
    @OneToMany(mappedBy = "produto")
    private List<IngredienteProduto> ingredientes = new ArrayList<>();
}
