package com.raizesdonordeste.api.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ingrediente")
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Size(max = 150)
    @NotBlank
    private String nome;
    @Column(name = "unidade_medida")
    @Size(max = 20)
    private String unidadeMedida;

    @OneToMany(mappedBy = "ingrediente")
    @JsonIgnore
    private List<EstoqueIngrediente> estoquesIngredientes = new ArrayList<>();

    @OneToMany(mappedBy = "ingrediente")
    @JsonIgnore
    private List<IngredienteProduto> ingredientesProduto = new ArrayList<>();
}
