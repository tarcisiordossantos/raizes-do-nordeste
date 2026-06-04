package com.raizesdonordeste.api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotBlank
    @Size(max = 10)
    private String cep;
    @Column
    @Size(max = 150)
    private String logradouro;
    @Column
    @Size(max = 10)
    private String numero;
    @Column
    @Size(max = 100)
    private String complemento;
    @Column
    @Size(max = 100)
    private String bairro;
    @Column
    @Size(max = 100)
    private String cidade;
    @Column
    @Size(max = 2, message = "deve ser informado apenas a sigla do estado com dois caracteres")
    private String estado;
    @Column
    private boolean principal;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario; 

    @OneToOne(mappedBy = "endereco")
    private Unidade unidade;

    public void alterarEndereco(String cep, String logradouro, String numero,
        String complemento, String bairro, String cidade, String estado) {
            this.cep = cep;
            this.logradouro = logradouro;
            this.numero = numero;
            this.complemento = complemento;
            this.bairro = bairro;
            this.cidade = cidade;
            this.estado = estado;
    }
}
