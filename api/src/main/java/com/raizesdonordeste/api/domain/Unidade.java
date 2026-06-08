package com.raizesdonordeste.api.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "unidade")
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotBlank
    @Size(max = 150)
    private String nome;
    @Column
    @NotBlank
    @Size(min = 14, max = 18)
    private String cnpj;
    @Column
    @Size(max = 20)
    private String telefone;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id")
    @JsonIgnoreProperties({"usuario","unidade"})
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "regiao_id")
    @JsonIgnoreProperties("unidades")
    private RegiaoComercial regiao;

    @OneToMany(mappedBy = "unidade", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("unidade")
    private List<Cardapio> cardapios  = new ArrayList<>();

    @OneToMany(mappedBy = "unidade", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<EstoqueProduto> estoquesProdutos  = new ArrayList<>();

    @OneToMany(mappedBy = "unidade", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<EstoqueIngrediente> estoquesIngredientes  = new ArrayList<>();

    @OneToMany(mappedBy = "unidade")
    @JsonIgnoreProperties({"unidade","usuario"})
    private List<Pedido> pedidos = new ArrayList<>();
}
