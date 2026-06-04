package com.raizesdonordeste.api.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Size(min = 11, max = 14)
    private String cpf;
    @Column(nullable = false)
    @NotBlank
    @Size(max = 150)
    private String nome;
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
    @Column(nullable = false, unique = true)
    @NotBlank
    @Email
    @Size(max = 150)
    private String email;
    @Column
    @Size(max = 20)
    private String telefone;
    @Column(nullable = false)
    @NotBlank
    @Size(max = 255)
    private String senha;
    @Column
    @Size(max = 30)
    private String genero;
    @Column(name = "pontos_fidelidade")
    private int pontosFidelidade;

    @ManyToMany
    @JoinTable(name = "perfil_usuario", 
    joinColumns = @JoinColumn(name = "usuario_id"),
    inverseJoinColumns = @JoinColumn(name = "perfil_id"))
    private List<Perfil> perfis  = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    private List<Endereco> enderecos  = new ArrayList<>();

    @OneToMany(mappedBy = "usuario")
    private List<ConsentimentoLgpd> consentimentos  = new ArrayList<>();

    public void alterarInformacoes(String nome, String email, String telefone) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public void alterarSenha(String senha) {
        this.senha = senha;
    }
}
